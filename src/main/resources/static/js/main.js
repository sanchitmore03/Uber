let map = null;
let vectorLayer = null;
let routeLayer = null;

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
        
        const role = document.getElementById('userRole').value;
        
        const signupData = {
            name: document.getElementById('signupName').value,
            email: document.getElementById('signupEmail').value,
            password: document.getElementById('signupPassword').value,
            role: role
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
            console.log('Signup response:', data);

            if (response.ok && data.data) {
                // Store the role for the subsequent login
                localStorage.setItem('lastSignupRole', role);
                
                alert('Signup successful! Please login.');
                signupModal.style.display = "none";
                loginModal.style.display = "block";
                
                // Pre-fill the login email
                document.getElementById('loginEmail').value = signupData.email;
                
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

    // Add driver registration modal
    const driverRegistrationModal = document.getElementById('driverRegistrationModal');
    const becomeDriverBtn = document.querySelector('.become-driver-btn');

    // Show driver registration modal
    becomeDriverBtn.onclick = () => driverRegistrationModal.style.display = "block";

    // Handle driver registration
    document.getElementById('driverRegistrationForm').addEventListener('submit', async (e) => {
        e.preventDefault();
        
        const driverData = {
            vehicleNumber: document.getElementById('vehicleNumber').value,
            vehicleModel: document.getElementById('vehicleModel').value,
            vehicleType: document.getElementById('vehicleType').value
        };

        try {
            const response = await fetch('/drivers/register', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
                },
                body: JSON.stringify(driverData)
            });

            const data = await response.json();

            if (response.ok) {
                alert('Successfully registered as a driver!');
                localStorage.setItem('isDriver', 'true');
                driverRegistrationModal.style.display = "none";
                updateUIAfterDriverRegistration();
            } else {
                alert(data.error?.message || 'Failed to register as driver');
            }
        } catch (error) {
            console.error('Error registering as driver:', error);
            alert('Failed to register as driver');
        }
    });

    // Update the login handler to remove role-based logic
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

            if (response.ok && data.data) {
                const userData = data.data;
                
                // Store user data
                localStorage.setItem('accessToken', userData.token);
                localStorage.setItem('userName', userData.name);
                localStorage.setItem('userId', userData.id);
                localStorage.setItem('isDriver', userData.isDriver || 'false');

                // Close modal and update UI
                loginModal.style.display = "none";
                updateUIAfterLogin();
            } else {
                alert(data.error?.message || 'Login failed');
            }
        } catch (error) {
            console.error('Login error:', error);
            alert('Login failed');
        }
    });

    // Update UI after login
    function updateUIAfterLogin() {
        const authButtons = document.querySelector('.auth-buttons');
        const becomeDriverBtn = document.querySelector('.become-driver-btn');
        const userName = localStorage.getItem('userName');
        const isDriver = localStorage.getItem('isDriver') === 'true';

        authButtons.innerHTML = `
            <span class="user-name">Welcome, ${userName || 'User'}</span>
            ${!isDriver ? '<button class="become-driver-btn">Become a Driver</button>' : ''}
            <button class="profile-btn">My Profile</button>
            <button class="logout-btn" onclick="logout()">Logout</button>
        `;

        if (isDriver) {
            initializeDriverDashboard();
        }
    }

    // Update UI after driver registration
    function updateUIAfterDriverRegistration() {
        const becomeDriverBtn = document.querySelector('.become-driver-btn');
        if (becomeDriverBtn) {
            becomeDriverBtn.remove();
        }
        initializeDriverDashboard();
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

        const token = localStorage.getItem('accessToken');
        if (!token) {
            alert("Please login first");
            loginModal.style.display = "block";
            return;
        }

        const paymentMethod = document.getElementById('paymentMethod').value;
        const rideRequest = {
            pickupLocation: {
                type: "Point",
                coordinates: [currentLocation.lng, currentLocation.lat],
                address: document.getElementById('pickupLocation').value
            },
            dropOffLocation: {
                type: "Point",
                coordinates: [selectedDropoffLocation.lng, selectedDropoffLocation.lat],
                address: selectedDropoffLocation.address
            },
            paymentMethod: paymentMethod,
            rideRequestStatus: "PENDING"
        };

        try {
            console.log('Sending ride request:', rideRequest);

            const response = await fetch('/riders/requestRide', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token.trim()}`
                },
                body: JSON.stringify(rideRequest)
            });

            const data = await response.json();
            console.log('Ride request response:', data);

            if (response.ok && data.data) {
                // Show the ride status section with map
                const mainContent = document.querySelector('main');
                const rideStatus = document.getElementById('rideStatus');
                
                mainContent.style.display = 'none';
                rideStatus.style.display = 'block';
                
                // Initialize map and show route
                const rideMap = initializeMap('rideMap');
                showRouteOnMap({
                    lat: currentLocation.lat,
                    lng: currentLocation.lng
                }, {
                    lat: selectedDropoffLocation.lat,
                    lng: selectedDropoffLocation.lng
                });

                // Start polling for ride status
                startRideStatusPolling(data.data.id);

                alert('Ride requested successfully! Waiting for a driver...');
            } else {
                if (response.status === 401) {
                    alert('Your session has expired. Please login again.');
                    localStorage.removeItem('accessToken');
                    location.reload();
                } else {
                    alert(data.error?.message || 'Failed to request ride');
                }
            }
        } catch (error) {
            console.error('Error requesting ride:', error);
            alert('Failed to request ride. Please try again.');
        }
    });

    // Add function to poll ride status
    function startRideStatusPolling(rideId) {
        const statusInterval = setInterval(async () => {
            try {
                const token = localStorage.getItem('accessToken');
                if (!token) {
                    clearInterval(statusInterval);
                    return;
                }

                const response = await fetch(`/riders/ride/${rideId}/status`, {
                    headers: {
                        'Authorization': `Bearer ${token.trim()}`
                    }
                });

                if (!response.ok) {
                    if (response.status === 401) {
                        clearInterval(statusInterval);
                        alert('Session expired. Please login again.');
                        localStorage.removeItem('accessToken');
                        location.reload();
                    }
                    return;
                }

                const data = await response.json();
                if (data.data) {
                    updateRideStatusUI(data.data);
                    
                    // Stop polling if ride is no longer pending
                    if (data.data.rideRequestStatus !== 'PENDING') {
                        clearInterval(statusInterval);
                    }
                }
            } catch (error) {
                console.error('Error polling ride status:', error);
            }
        }, 5000); // Poll every 5 seconds
    }

    // Add function to update ride status UI
    function updateRideStatusUI(rideData) {
        const statusContent = document.querySelector('.status-content h3');
        const driverInfo = document.getElementById('driverInfo');
        
        switch (rideData.rideRequestStatus) {
            case 'ACCEPTED':
                statusContent.textContent = 'Driver Found!';
                driverInfo.style.display = 'block';
                if (rideData.driver) {
                    document.getElementById('driverName').textContent = `Driver: ${rideData.driver.name}`;
                    document.getElementById('estimatedTime').textContent = 
                        `Estimated arrival: ${calculateEstimatedTime(rideData)} minutes`;
                }
                break;
            case 'STARTED':
                statusContent.textContent = 'Ride in Progress';
                break;
            case 'COMPLETED':
                statusContent.textContent = 'Ride Completed';
                setTimeout(() => {
                    location.reload();
                }, 3000);
                break;
            case 'CANCELLED':
                statusContent.textContent = 'Ride Cancelled';
                setTimeout(() => {
                    location.reload();
                }, 3000);
                break;
            default:
                statusContent.textContent = 'Finding Driver...';
        }
    }

    // Helper function to calculate estimated time
    function calculateEstimatedTime(rideData) {
        // Simple estimation based on distance
        // Assuming average speed of 30 km/h
        const distance = calculateDistance(
            rideData.pickupLocation,
            rideData.dropOffLocation
        );
        return Math.round((distance / 30) * 60); // Convert to minutes
    }

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

    // Initialize Google Maps
    initializeMap('map');
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
    const iconData = 'AAABAAEAEBAAAAEAIABoBAAAFgAAACgAAAAQAAAAIAAAAAEAIAAAAAAAAAQAABILAAASCwAAAAAAAAAAAAD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A////AP///wD///8A';

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

// Initialize Google Maps
function initializeMap(containerId) {
    // Create vector layer for markers
    vectorLayer = new ol.layer.Vector({
        source: new ol.source.Vector()
    });

    // Create vector layer for route
    routeLayer = new ol.layer.Vector({
        source: new ol.source.Vector()
    });

    // Create map
    map = new ol.Map({
        target: containerId,
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
            }),
            routeLayer,
            vectorLayer
        ],
        view: new ol.View({
            center: ol.proj.fromLonLat([0, 0]),
            zoom: 12
        })
    });

    return map;
}

// Add function to show markers
function addMarker(coordinates, type) {
    const feature = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([coordinates.lng, coordinates.lat]))
    });

    // Style the marker based on type (pickup/dropoff)
    const style = new ol.style.Style({
        image: new ol.style.Circle({
            radius: 7,
            fill: new ol.style.Fill({
                color: type === 'pickup' ? '#4CAF50' : '#f44336'
            }),
            stroke: new ol.style.Stroke({
                color: '#fff',
                width: 2
            })
        })
    });

    feature.setStyle(style);
    vectorLayer.getSource().addFeature(feature);
}

// Update showRouteOnMap function
async function showRouteOnMap(pickup, dropoff) {
    // Clear existing route
    routeLayer.getSource().clear();
    vectorLayer.getSource().clear();

    // Add markers
    addMarker(pickup, 'pickup');
    addMarker(dropoff, 'dropoff');

    try {
        // Get route using OSRM
        const response = await fetch(
            `https://router.project-osrm.org/route/v1/driving/` +
            `${pickup.lng},${pickup.lat};${dropoff.lng},${dropoff.lat}` +
            `?overview=full&geometries=geojson`
        );
        
        const data = await response.json();
        
        if (data.routes && data.routes.length > 0) {
            const route = data.routes[0];
            const routeFeature = new ol.Feature({
                geometry: new ol.geom.LineString(route.geometry.coordinates)
                    .transform('EPSG:4326', 'EPSG:3857')
            });

            // Style the route
            routeFeature.setStyle(new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: '#2196F3',
                    width: 4
                })
            }));

            routeLayer.getSource().addFeature(routeFeature);

            // Fit map to route extent
            map.getView().fit(routeLayer.getSource().getExtent(), {
                padding: [50, 50, 50, 50]
            });
        }
    } catch (error) {
        console.error('Error getting route:', error);
    }
}

// Update displayAvailableRides function
function displayAvailableRides(rides) {
    const container = document.getElementById('availableRides');
    container.innerHTML = '';
    
    // Clear existing markers and routes
    vectorLayer.getSource().clear();
    routeLayer.getSource().clear();
    
    rides.forEach(ride => {
        const card = document.createElement('div');
        card.className = 'ride-card';
        card.innerHTML = `
            <p>From: ${ride.pickupLocation.address}</p>
            <p>To: ${ride.dropOffLocation.address}</p>
            <p>Distance: ${calculateDistance(ride.pickupLocation, ride.dropOffLocation)} km</p>
            <button onclick="acceptRide(${ride.id})" class="action-btn">Accept Ride</button>
        `;
        container.appendChild(card);
        
        // Show pickup and dropoff points on map
        const pickup = {
            lat: ride.pickupLocation.coordinates[1],
            lng: ride.pickupLocation.coordinates[0]
        };
        const dropoff = {
            lat: ride.dropOffLocation.coordinates[1],
            lng: ride.dropOffLocation.coordinates[0]
        };
        
        addMarker(pickup, 'pickup');
        addMarker(dropoff, 'dropoff');
    });

    // Fit map to show all markers
    if (vectorLayer.getSource().getFeatures().length > 0) {
        map.getView().fit(vectorLayer.getSource().getExtent(), {
            padding: [50, 50, 50, 50]
        });
    }
}

// Driver Dashboard Functions
async function initializeDriverDashboard() {
    console.log('Initializing driver dashboard');
    try {
        // Initialize map
        map = initializeMap('map');
        
        // Load initial rides
        await loadAvailableRides();
        
        // Set up polling for new rides
        setInterval(loadAvailableRides, 10000);
    } catch (error) {
        console.error('Error initializing driver dashboard:', error);
    }
}

async function loadAvailableRides() {
    try {
        const response = await fetch('/drivers/available-rides', {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        });
        
        const rides = await response.json();
        displayAvailableRides(rides);
    } catch (error) {
        console.error('Error loading rides:', error);
    }
}

async function acceptRide(rideId) {
    try {
        const response = await fetch(`/drivers/accept-ride/${rideId}`, {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        });
        
        if (response.ok) {
            const ride = await response.json();
            showActiveRide(ride);
        }
    } catch (error) {
        console.error('Error accepting ride:', error);
    }
}

// Rider Functions
function showRideStatus(rideRequest) {
    const statusDiv = document.getElementById('rideStatus');
    statusDiv.style.display = 'block';
    
    // Initialize map for rider
    const rideMap = initializeMap('rideMap');
    
    // Show initial route
    showRouteOnMap(rideRequest.pickupLocation, rideRequest.dropOffLocation);
    
    // Poll for ride status updates
    const statusCheck = setInterval(async () => {
        const response = await fetch(`/riders/ride-status/${rideRequest.id}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
            }
        });
        
        const status = await response.json();
        
        if (status.driver) {
            clearInterval(statusCheck);
            showDriverAssigned(status.driver);
        }
    }, 5000);
}

function showDriverAssigned(driver) {
    const driverInfo = document.getElementById('driverInfo');
    driverInfo.style.display = 'block';
    document.getElementById('driverName').textContent = `Driver: ${driver.name}`;
    // Update estimated time based on distance calculation
}

// Utility Functions
function calculateDistance(point1, point2) {
    const lat1 = point1.coordinates[1];
    const lon1 = point1.coordinates[0];
    const lat2 = point2.coordinates[1];
    const lon2 = point2.coordinates[0];
    
    // Haversine formula
    const R = 6371; // Earth's radius in km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) * 
              Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return (R * c).toFixed(2);
}

// Add a function to clear signup data after successful login
function clearSignupData() {
    localStorage.removeItem('lastSignupRole');
}