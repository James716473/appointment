CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  pass VARCHAR(255) NOT NULL,
  sex ENUM('M', 'F') NOT NULL,
  birth_date DATE NOT NULL,
  contact_number VARCHAR(12),
  medical_history TEXT,
  allergies TEXT,
  family_medical_history TEXT
);

CREATE TABLE IF NOT EXISTS doctors (
  doctor_id INT AUTO_INCREMENT PRIMARY KEY,
  first_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL UNIQUE,
  pass VARCHAR(255) NOT NULL,
  sex ENUM('M', 'F') NOT NULL,
  birth_date DATE NOT NULL,
  contact_number VARCHAR(12),
  specialty VARCHAR(255),
  affiliation VARCHAR(255),
  schedule_from TIME DEFAULT '10:00:00',
  schedule_to TIME DEFAULT '17:00:00'
);

CREATE TABLE IF NOT EXISTS admn (
  email VARCHAR(255) NOT NULL UNIQUE,
  pass VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS affiliates (
  affiliate_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL UNIQUE,
  address VARCHAR(255) NOT NULL,
  description TEXT 
);

CREATE TABLE IF NOT EXISTS appointments (
  appointment_id INT AUTO_INCREMENT PRIMARY KEY,
  user_id INT NOT NULL,
  doctor_id INT NOT NULL,
  appointment_date DATE NOT NULL,
  appointment_time TIME NOT NULL,
  appointment_type VARCHAR(255) NOT NULL,
  billing_id INT NOT NULL,
  description TEXT
);

CREATE TABLE IF NOT EXISTS billings (
  billing_id INT AUTO_INCREMENT PRIMARY KEY,
  price DECIMAL(10, 2) DEFAULT 99999.99,
  status ENUM('Paid', 'Not_Paid') DEFAULT 'Not_Paid',
  date_issued DATE NOT NULL,
  date_paid DATE 
);


