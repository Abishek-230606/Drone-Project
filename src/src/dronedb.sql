USE dronedb;


CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

INSERT INTO users (username, password) VALUES
('admin', 'admin123'),
('havish', 'hav123'),
('abishek', 'abi123');

CREATE TABLE blood_inventory (
    blood_id INT AUTO_INCREMENT PRIMARY KEY,
    blood_type VARCHAR(5) NOT NULL,
    quantity_units INT NOT NULL,
    location VARCHAR(100)
);

INSERT INTO blood_inventory (blood_type, quantity_units, location) VALUES
('A+', 10, 'tambaram'),
('B-', 5, 'vandalur'),
('O+', 15, 'srm'),
('AB+', 8, 'mm_nagar');

CREATE TABLE delivery_requests (
    request_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_name VARCHAR(100),
    blood_type VARCHAR(5),
    units_requested INT,
    delivery_address VARCHAR(255),
    status VARCHAR(20) DEFAULT 'Pending'
);

CREATE TABLE organ_inventory (
    id INT AUTO_INCREMENT PRIMARY KEY,
    organ_type VARCHAR(50) NOT NULL,       -- e.g., "Kidney", "Liver"
    quantity INT NOT NULL,                 -- e.g., 2 units
    urgency_level VARCHAR(20),            -- e.g., "High", "Medium", "Low"
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO organ_inventory (organ_type, quantity, urgency_level) VALUES
('Kidney', 2, 'High'),
('Liver', 1, 'Medium'),
('Cornea', 4, 'Low');

CREATE TABLE registered_hospitals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hospital_name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    contact_number VARCHAR(20),
    registration_date DATE
);

INSERT INTO registered_hospitals (hospital_name, address, contact_number, registration_date) VALUES
('Apollo Hospital', 'No. 21, Greams Lane, Chennai, Tamil Nadu 600006', '044 2829 3333', '2025-01-15'),
('Deepam Hospital', 'GST Road, West Tambaram, Chennai, Tamil Nadu 600045', '044 2226 2226', '2025-03-10'),
('RKP MULTI-SPECIALITY HOSPITAL', '1 62, GST Road, Near Periyar University, Guduvancheri, Tamil Nadu 603203', '044 2746 5366', '2025-05-05');
 
 ALTER TABLE registered_hospitals
ADD COLUMN hospital_id VARCHAR(50) UNIQUE NOT NULL,
ADD COLUMN password VARCHAR(50) NOT NULL;


DROP TABLES registered_hospitals;

CREATE TABLE registered_hospitals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    hospital_name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    contact_number VARCHAR(20),
    registration_date DATE
);

ALTER TABLE registered_hospitals
ADD COLUMN hospital_id VARCHAR(50) UNIQUE NOT NULL,
ADD COLUMN password VARCHAR(50) NOT NULL;

INSERT INTO registered_hospitals 
(hospital_name, address, contact_number, registration_date, hospital_id, password) 
VALUES
('Apollo Hospital', 'No. 21, Greams Lane, Chennai, Tamil Nadu 600006', '044 2829 3333', '2025-01-15', 'apollo001', 'apollo@123'),
('Deepam Hospital', 'GST Road, West Tambaram, Chennai, Tamil Nadu 600045', '044 2226 2226', '2025-03-10', 'deepam002', 'deepam@123'),
('SRM Medical College', 'SRM Nagar, Kattankulathur, Tamil Nadu 603203', '044 2745 5510', '2025-05-05', 'srm003', 'srm@123');






