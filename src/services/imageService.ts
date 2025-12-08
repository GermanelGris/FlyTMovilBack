import sharp from 'sharp';

export async function processImageForDB(buffer: Buffer) {
  console.log('🖼️ Iniciando procesamiento de imagen...');
  console.log('📊 Tamaño original:', buffer.length, 'bytes');

  const outputBuffer = await sharp(buffer)
    .resize(512, 512, { fit: 'cover' })
    .webp({ quality: 80 })
    .toBuffer();

  const meta = await sharp(outputBuffer).metadata();

  console.log('✅ Imagen procesada:', {
    tamañoOriginal: buffer.length,
    tamañoFinal: outputBuffer.length,
    reducción: `${((1 - outputBuffer.length / buffer.length) * 100).toFixed(2)}%`,
    dimensiones: `${meta.width}x${meta.height}`
  });

  return {
    buffer: outputBuffer,
    size: outputBuffer.length,
    width: meta.width,
    height: meta.height,
    mime: 'image/webp',
  };
}
