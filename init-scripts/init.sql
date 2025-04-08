-- Create databases if they don't exist
CREATE DATABASE IF NOT EXISTS `msvc-students`;
CREATE DATABASE IF NOT EXISTS `msvc-auth`;

-- Grant privileges
GRANT ALL PRIVILEGES ON `msvc-students`.* TO 'root'@'%';
GRANT ALL PRIVILEGES ON `msvc-auth`.* TO 'root'@'%';
FLUSH PRIVILEGES;