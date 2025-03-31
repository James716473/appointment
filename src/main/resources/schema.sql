CREATE TABLE IF NOT EXISTS users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  last_name VARCHAR(255) NOT NULL,
  first_name VARCHAR(255) NOT NULL,
  middle_name VARCHAR(255),
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  sex ENUM('M', 'F') NOT NULL,
  birth_date DATE NOT NULL,
  role ENUM('User', 'Doctor') NOT NULL
);



CREATE TABLE IF NOT EXISTS records (
  record_id INT AUTO_INCREMENT PRIMARY KEY,
  type VARCHAR(255) NOT NULL,
  record_date DATE NOT NULL,
  image_url VARCHAR(255) NOT NULL,
  description TEXT NOT NULL,
  user_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS appointments (
  appointment_id INT AUTO_INCREMENT PRIMARY KEY,
  appointment_date DATE NOT NULL,
  appointment_time TIME NOT NULL,
  doctor_id INT NOT NULL,
  user_id INT NOT NULL,
  FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS payments (
  payment_id INT AUTO_INCREMENT PRIMARY KEY,
  amount DECIMAL(10, 2) NOT NULL,
  type VARCHAR(255) NOT NULL,
  payment_date DATE NOT NULL,
  user_id INT NOT NULL,
  doctor_id INT NOT NULL,
  appointment_id INT NOT NULL,
  FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id), 
  FOREIGN KEY (user_id) REFERENCES users(id)
);
