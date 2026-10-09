MERGE INTO users (id, name, email, password_hash, role)
KEY(email) VALUES (1, 'KrishMart Admin', 'admin@krishmart.local', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'ADMIN');
MERGE INTO users (id, name, email, password_hash, role)
KEY(email) VALUES (2, 'GreenNest Seller', 'seller@krishmart.local', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'SELLER');
MERGE INTO users (id, name, email, password_hash, role)
KEY(email) VALUES (3, 'Demo Buyer', 'buyer@krishmart.local', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'BUYER');

MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (1, 2, 'Botanical Ceramic Planter', 'A modern ceramic planter for a fresh desk or sunny windowsill.', 349.00, 24, 'Green Living', 'https://images.unsplash.com/photo-1416879595882-3373a0480b5b?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (2, 2, 'Reusable Glass Bottle', 'A clear reusable bottle for everyday hydration on the go.', 499.00, 30, 'Eco Essentials', 'https://images.unsplash.com/photo-1523362628745-0c100150b504?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (3, 2, 'Natural Cotton Market Tote', 'Lightweight reusable tote for groceries, books, and daily errands.', 279.00, 40, 'Bags & Carry', 'https://images.unsplash.com/photo-1590874103328-eac38a683ce7?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (4, 2, 'Minimal Desk Organizer', 'Keep stationery and small accessories neat with this compact organizer.', 599.00, 18, 'Workspace', 'https://images.unsplash.com/photo-1498050108023-c5249f4df085?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (5, 2, 'Soy Wax Calm Candle', 'A softly scented soy wax candle for a peaceful evening routine.', 429.00, 22, 'Home & Calm', 'https://images.unsplash.com/photo-1603006905003-be475563bc59?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (6, 2, 'Bamboo Toothbrush Set', 'A simple low-waste everyday care set with a natural bamboo handle.', 199.00, 50, 'Eco Essentials', 'https://images.unsplash.com/photo-1609840114035-3c981b782dfe?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (7, 2, 'Leaf Print Cushion Cover', 'Bring a botanical accent to your sofa or reading corner.', 399.00, 16, 'Home & Calm', 'https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (8, 2, 'Insulated Lunch Container', 'A compact food container for campus, work, or day trips.', 649.00, 20, 'Kitchen & Dining', 'https://images.unsplash.com/photo-1606787366850-de6330128bfc?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (9, 2, 'Recycled Paper Journal', 'A thoughtfully designed journal for notes, sketches, and plans.', 249.00, 35, 'Workspace', 'https://images.unsplash.com/photo-1517842645767-c639042777db?w=900');
MERGE INTO products (id, seller_id, name, description, price, stock_qty, category, image_url)
KEY(id) VALUES (10, 2, 'Soft Yoga Stretch Mat', 'Comfortable grip mat for gentle stretching and daily movement.', 999.00, 14, 'Wellness', 'https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=900');

ALTER TABLE users ALTER COLUMN id RESTART WITH 1000;
ALTER TABLE products ALTER COLUMN id RESTART WITH 1000;
