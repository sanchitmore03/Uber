document.addEventListener('DOMContentLoaded', function() {
    // Modal elements
    const loginModal = document.getElementById('loginModal');
    const signupModal = document.getElementById('signupModal');
    const loginBtn = document.querySelector('.login-btn');
    const signupBtn = document.querySelector('.signup-btn');
    const closeBtns = document.querySelectorAll('.close');
    
    // Show modals
    loginBtn.onclick = () => loginModal.style.display = "block";
    signupBtn.onclick = () => signupModal.style.display = "block";
    
    // Close modals
    closeBtns.forEach(btn => {
        btn.onclick = function() {
            loginModal.style.display = "none";
            signupModal.style.display = "none";
        }
    });

    // Handle signup
    document.getElementById('signupForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const signupData = {
            name: document.getElementById('signupName').value,
            email: document.getElementById('signupEmail').value,
            password: document.getElementById('signupPassword').value,
            role: document.getElementById('userRole').value
        };

        try {
            const response = await fetch('/auth/signup', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(signupData)
            });

            if (response.ok) {
                const data = await response.json();
                alert('Signup successful! Please login.');
                signupModal.style.display = "none";
                loginModal.style.display = "block";
            } else {
                const error = await response.json();
                alert(error.message || 'Signup failed');
            }
        } catch (error) {
            console.error('Signup error:', error);
            alert('Signup failed. Please try again.');
        }
    });

    // Handle login
    document.getElementById('loginForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const loginData = {
            email: document.getElementById('loginEmail').value,
            password: document.getElementById('loginPassword').value
        };

        try {
            const response = await fetch('/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginData)
            });

            if (response.ok) {
                const data = await response.json();
                // Store the access token
                localStorage.setItem('accessToken', data.accessToken);
                
                // Close modal and update UI
                loginModal.style.display = "none";
                updateUIAfterLogin();
                
                // Redirect based on role if needed
                // checkUserRoleAndRedirect();
            } else {
                const error = await response.json();
                alert(error.message || 'Login failed');
            }
        } catch (error) {
            console.error('Login error:', error);
            alert('Login failed. Please try again.');
        }
    });

    // Function to update UI after login
    function updateUIAfterLogin() {
        const authButtons = document.querySelector('.auth-buttons');
        authButtons.innerHTML = `
            <button class="profile-btn">My Profile</button>
            <button class="logout-btn" onclick="logout()">Logout</button>
        `;
    }

    // Global logout function
    window.logout = function() {
        localStorage.removeItem('accessToken');
        location.reload();
    }

    // Add authentication header to all API requests
    function addAuthHeader(headers = {}) {
        const token = localStorage.getItem('accessToken');
        if (token) {
            headers['Authorization'] = `Bearer ${token}`;
        }
        return headers;
    }

    // Example of making authenticated API request
    async function makeAuthenticatedRequest(url, options = {}) {
        const token = localStorage.getItem('accessToken');
        if (!token) {
            throw new Error('No authentication token found');
        }

        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${token}`
        };

        const response = await fetch(url, options);
        if (response.status === 401) {
            localStorage.removeItem('accessToken');
            location.reload();
            return null;
        }
        return response;
    }

    let currentLocation = null;
    let selectedDropoffLocation = null;

    // Improved device location handling
    const getLocationBtn = document.getElementById('getLocationBtn');
    const pickupLocationInput = document.getElementById('pickupLocation');

    function getCurrentLocation() {
        return new Promise((resolve, reject) => {
            if (!navigator.geolocation) {
                reject(new Error("Geolocation is not supported by your browser."));
                return;
            }

            const options = {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            };

            navigator.geolocation.getCurrentPosition(
                (position) => resolve(position),
                (error) => reject(error),
                options
            );
        });
    }

    getLocationBtn.addEventListener('click', async () => {
        pickupLocationInput.value = "Getting location...";
        try {
            const position = await getCurrentLocation();
            currentLocation = {
                lat: position.coords.latitude,
                lng: position.coords.longitude
            };
            
            // Get address from coordinates
            const address = await reverseGeocode(currentLocation);
            pickupLocationInput.value = address;
        } catch (error) {
            console.error("Error getting location:", error);
            pickupLocationInput.value = "";
            alert("Unable to get your location. Please allow location access.");
        }
    });

    // Destination search functionality
    const dropoffInput = document.getElementById('dropoffLocation');
    const searchResults = document.getElementById('searchResults');
    let searchTimeout;

    dropoffInput.addEventListener('input', (e) => {
        clearTimeout(searchTimeout);
        const query = e.target.value;
        
        if (query.length < 3) {
            searchResults.style.display = 'none';
            return;
        }

        searchTimeout = setTimeout(() => searchLocations(query), 500);
    });

    async function searchLocations(query) {
        try {
            const response = await fetch(
                `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(query)}&limit=5`
            );
            const data = await response.json();
            
            searchResults.innerHTML = '';
            searchResults.style.display = data.length ? 'block' : 'none';

            data.forEach(result => {
                const div = document.createElement('div');
                div.className = 'search-result-item';
                div.textContent = result.display_name;
                div.addEventListener('click', () => {
                    selectedDropoffLocation = {
                        lat: parseFloat(result.lat),
                        lng: parseFloat(result.lon),
                        address: result.display_name
                    };
                    dropoffInput.value = result.display_name;
                    document.getElementById('dropoffLat').value = result.lat;
                    document.getElementById('dropoffLng').value = result.lon;
                    searchResults.style.display = 'none';
                });
                searchResults.appendChild(div);
            });
        } catch (error) {
            console.error('Error searching locations:', error);
        }
    }

    // Close search results when clicking outside
    document.addEventListener('click', (e) => {
        if (!dropoffInput.contains(e.target) && !searchResults.contains(e.target)) {
            searchResults.style.display = 'none';
        }
    });

    // Update the ride request handler
    document.querySelector('.request-ride-btn').addEventListener('click', async () => {
        if (!currentLocation) {
            alert("Please set your pickup location first");
            return;
        }

        if (!selectedDropoffLocation) {
            alert("Please select a destination from the search results");
            return;
        }

        const token = localStorage.getItem('accessToken');
        if (!token) {
            alert("Please login first");
            loginModal.style.display = "block";
            return;
        }

        const paymentMethod = document.getElementById('paymentMethod').value;

        const rideRequest = {
            pickupLocation: {
                coordinates: [currentLocation.lng, currentLocation.lat]  // GeoJSON format: [longitude, latitude]
            },
            dropOffLocation: {
                coordinates: [selectedDropoffLocation.lng, selectedDropoffLocation.lat]
            },
            paymentMethod: paymentMethod  // Note: fixed typo in property name
        };

        try {
            const response = await fetch('/api/rides/request', {  // Updated endpoint
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(rideRequest)
            });

            if (response.ok) {
                const data = await response.json();
                alert('Ride requested successfully! A driver will be assigned shortly.');
                console.log('Ride request response:', data);
            } else {
                const error = await response.json();
                console.error('Ride request error:', error);
                alert(error.message || 'Failed to request ride');
            }
        } catch (error) {
            console.error('Error requesting ride:', error);
            alert('Failed to request ride. Please try again.');
        }
    });

    // Helper function for reverse geocoding
    async function reverseGeocode(location) {
        try {
            const response = await fetch(
                `https://nominatim.openstreetmap.org/reverse?lat=${location.lat}&lon=${location.lng}&format=json`
            );
            const data = await response.json();
            return data.display_name || `${location.lat.toFixed(6)}, ${location.lng.toFixed(6)}`;
        } catch (error) {
            console.error("Error reverse geocoding:", error);
            return `${location.lat.toFixed(6)}, ${location.lng.toFixed(6)}`;
        }
    }
});