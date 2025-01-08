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

            const data = await response.json();
            console.log('Signup response:', data); // Debug log

            if (response.ok && data.data) {
                alert('Signup successful! Please login.');
                signupModal.style.display = "none";
                loginModal.style.display = "block";
                
                // Clear the signup form
                document.getElementById('signupForm').reset();
            } else {
                console.error('Signup error:', data);
                alert(data.error?.message || 'Signup failed. Please try again.');
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

            const data = await response.json();
            console.log('Raw login response:', data); // Debug log to see exact structure

            if (response.ok) {
                // Handle LoginResponseDto structure
                let token = null;
                
                // Try different response structures
                if (data.accessToken) {
                    token = data.accessToken;
                } else if (data.data && data.data.accessToken) {
                    token = data.data.accessToken;
                } else if (data.token) {
                    token = data.token;
                } else if (data.data && data.data.token) {
                    token = data.data.token;
                }

                console.log('Extracted token:', token); // Debug log

                if (!token) {
                    console.error('Response structure:', data);
                    throw new Error('Token not found in response. Check console for details.');
                }

                // Store the cleaned token
                localStorage.setItem('accessToken', token.toString().trim());
                
                // Close modal and update UI
                loginModal.style.display = "none";
                updateUIAfterLogin();
            } else {
                // Handle error response
                const errorMessage = data.error?.message || data.message || 'Login failed';
                console.error('Login error details:', data);
                alert(errorMessage);
            }
        } catch (error) {
            console.error('Login error:', error);
            alert('Login failed: ' + error.message);
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
        if (!token || !isValidToken(token)) {
            localStorage.removeItem('accessToken');
            alert("Please login again");
            loginModal.style.display = "block";
            throw new Error('Invalid or missing token');
        }

        // Remove any whitespace from token
        const cleanToken = token.trim();
        
        options.headers = {
            ...options.headers,
            'Authorization': `Bearer ${cleanToken}`
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

        const token = localStorage.getItem('accessToken')?.trim(); // Add trim here
        if (!token) {
            alert("Please login first");
            loginModal.style.display = "block";
            return;
        }

        const paymentMethod = document.getElementById('paymentMethod').value;

        // Format request according to your existing RideRequestDto structure
        const rideRequest = {
            id: null,
            pickupLocation: {
                type: "Point",
                coordinates: [currentLocation.lng, currentLocation.lat]
            },
            dropOffLocation: {
                type: "Point",
                coordinates: [selectedDropoffLocation.lng, selectedDropoffLocation.lat]
            },
            requestedTime: new Date().toISOString(),
            rider: null,  // This will be set by backend
            fare: null,   // This will be calculated by backend
            paymentMethod: paymentMethod,
            rideRequestStatus: "REQUESTED"
        };

        try {
            console.log('Token being sent:', token);
            const response = await fetch('/riders/requestRide', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}` // No need for trim() here since we did it above
                },
                body: JSON.stringify(rideRequest)
            });

            if (response.ok) {
                const data = await response.json();
                console.log('Ride request response:', data);
                alert('Ride requested successfully! A driver will be assigned shortly.');
                
                // Clear the form
                document.getElementById('dropoffLocation').value = '';
                document.getElementById('pickupLocation').value = '';
                currentLocation = null;
                selectedDropoffLocation = null;
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

    // Check if user is already logged in
    const token = localStorage.getItem('accessToken');
    if (token) {
        // Verify token validity
        verifyToken(token);
    }

    // Add favicon programmatically
    addFavicon();
});

// Add this function to verify token
async function verifyToken(token) {
    try {
        const cleanToken = token.trim(); // Add trim here
        const response = await fetch('/auth/verify', {
            headers: {
                'Authorization': `Bearer ${cleanToken}`
            }
        });
        
        if (!response.ok) {
            localStorage.removeItem('accessToken');
            location.reload();
        } else {
            updateUIAfterLogin();
        }
    } catch (error) {
        console.error('Token verification error:', error);
        localStorage.removeItem('accessToken');
        location.reload();
    }
}

// Add this utility function
function isValidToken(token) {
    if (!token) return false;
    
    // Check if token has the correct JWT format (three parts separated by dots)
    const parts = token.split('.');
    if (parts.length !== 3) return false;
    
    return true;
}

// Add error handling for fetch requests
function handleResponse(response) {
    return response.json().then(data => {
        if (!response.ok) {
            // If the server returned an error, throw it
            throw new Error(data.error?.message || 'Network response was not ok');
        }
        return data;
    });
}

function setLoading(form, isLoading) {
    const submitBtn = form.querySelector('button[type="submit"]');
    if (isLoading) {
        submitBtn.disabled = true;
        submitBtn.textContent = 'Please wait...';
    } else {
        submitBtn.disabled = false;
        submitBtn.textContent = submitBtn.getAttribute('data-original-text') || 'Submit';
    }
}

// Add this function to create and add favicon
function addFavicon() {
    // Base64 encoded small black icon
    const iconData = 'AAABAAEAEBAAAAEAIABoBAAAFgAAACgAAAAQAAAAIAAAAAEAIAAAAAAAAAQAABILAAASCwAAAAAAAAAAAAD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A';

    // Convert base64 to blob
    const byteCharacters = atob(iconData);
    const byteNumbers = new Array(byteCharacters.length);
    for (let i = 0; i < byteCharacters.length; i++) {
        byteNumbers[i] = byteCharacters.charCodeAt(i);
    }
    const byteArray = new Uint8Array(byteNumbers);
    const blob = new Blob([byteArray], {type: 'image/x-icon'});

    // Create URL for the blob
    const iconUrl = URL.createObjectURL(blob);

    // Create or update favicon link
    let link = document.querySelector("link[rel*='icon']") || document.createElement('link');
    link.type = 'image/x-icon';
    link.rel = 'shortcut icon';
    link.href = iconUrl;
    document.getElementsByTagName('head')[0].appendChild(link);
}