-- Este archivo añade columnas para almacenar URL/metadata si prefieres no usar BLOB.
ALTER TABLE users
  ADD COLUMN IF NOT EXISTS profile_photo_url TEXT,
  ADD COLUMN IF NOT EXISTS profile_photo_key TEXT,
  ADD COLUMN IF NOT EXISTS profile_photo_mime VARCHAR(50),
  ADD COLUMN IF NOT EXISTS profile_photo_size INT,
  ADD COLUMN IF NOT EXISTS profile_photo_width INT,
  ADD COLUMN IF NOT EXISTS profile_photo_height INT;
