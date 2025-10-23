🛩️ Drone-Based Medical Delivery Platform
Overview
This project simulates a real-world medical logistics system using drones to deliver critical supplies—like blood and organs—between hospitals. It’s designed to be secure, scalable, and authentic, with realistic routing, inventory management, and role-based access.
Whether you're a developer, healthcare partner, or curious contributor, this README walks you through the system’s architecture, features, and setup.


🚀 Key Features
- Secure Hospital Login
Role-based authentication for hospitals and admins using hashed credentials and session tracking.
- Live Route Preview
Integrates OpenRouteService API to calculate real-time distance, delivery time, and visualize drone paths.
- Inventory Management
Separate SQL tables for blood and organ stock, dynamically updated per hospital.
- Order Routing Workflow
Multi-step order flow: select item → choose destination → preview route → confirm delivery.
- Admin Dashboard
Analytics on delivery history, hospital activity, and inventory trends.
- Scalable Architecture
Modular Java classes, JDBC-SQL integration, and Swing UI for desktop simulation.


🧠 System Architecture

+------------------+       +------------------+       +------------------+
|  Hospital Login  | --->  |  Order Workflow  | --->  |  Route Preview   |
+------------------+       +------------------+       +------------------+
        |                        |                            |
        v                        v                            v
+------------------+       +------------------+       +------------------+
|  Inventory Table |       |  Delivery Table  |       |  OpenRoute API   |
+------------------+       +------------------+       +------------------+


- Frontend: Java Swing UI with multi-page navigation
- Backend: Java classes + JDBC for SQL operations
- Database: MySQL with normalized tables for hospitals, inventory, and deliveries
- API: OpenRouteService for route simulation


🏥 User Roles
|Role |Capabilities  | 
|Admin  |View analytics, manage hospitals, monitor deliveries  | 
|Hospital  |Login, place orders, view inventory, preview routes  | 

📦 Inventory Schema
- Blood Table: Tracks blood type, quantity, expiry, hospital ID
- Organ Table: Tracks organ type, availability, donor ID, hospital ID
Each hospital has its own inventory, updated in real time as orders are placed and fulfilled.

📍 Route Simulation
- Uses OpenRouteService API to:
- Calculate distance and estimated delivery time
- Display route on map (via embedded browser or image preview)
- Validate feasibility based on drone range

🔐 Security Highlights
- Password hashing with SHA-256
- Role-based access control
- SQL injection prevention via prepared statements
- Session tokens for authenticated flows


🛠️ Setup Instructions
- Clone the repo
git clone https:[//github.com/your-username/drone-medical-delivery.git](https://github.com/Abishek-230606/Drone-Project)

- Configure MySQL
- Create database medical_delivery
- Import schema from /sql/schema.sql
- Set up API keys
- Get OpenRouteService API key
- Add to config.properties
- Run the app
javac Main.java
java Main




 Future Enhancements
- Drone battery and range simulation
- Emergency prioritization logic
- Real-time delivery tracking
- REST API for mobile/web integration

🤝 Contributing
We welcome collaborators! Whether you're into UI polish, backend optimization, or healthcare innovation—there’s room to grow.

📬 Contact
For questions, partnerships, or demo requests:
Abishek JS
📧 jsabishek236@gmail.com






