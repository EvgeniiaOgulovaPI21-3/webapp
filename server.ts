import * as http from 'http';
import { parse } from 'url';
import { readFile } from 'fs/promises';
import { getUploadUrl, uploadFile, getFileContent, deleteFile } from './yandexDiskApi';
import * as formidable from 'formidable';
import * as dotenv from 'dotenv';
import { exec } from 'child_process';
import { promisify } from 'util';
import mammoth from 'mammoth';

dotenv.config();

const execPromise = promisify(exec);

const accessToken: string = process.env.YANDEX_DISK_TOKEN || ''; // Ваш токен из переменных окружения

if (!accessToken) {
    console.error('No access token provided. Set YANDEX_DISK_TOKEN in the .env file.');
    process.exit(1);
}

// Конфигурация допустимых расширений, типов файлов и максимального размера
const allowedExtensions = ['.docx'];
const allowedMimeTypes = ['application/vnd.openxmlformats-officedocument.wordprocessingml.document'];
const maxFileSize = 50 * 1024; // 50 кб

const requestListener: http.RequestListener = async (req: http.IncomingMessage, res: http.ServerResponse) => {
    const parsedUrl = parse(req.url || '', true);
    const pathname = parsedUrl.pathname;

    if (pathname === '/') {
        try {
            const html = await readFile('index.html', 'utf-8');
            res.writeHead(200, { 'Content-Type': 'text/html' });
            res.end(html);
        } catch (error) {
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Error loading the page.');
        }
    } else if (pathname === '/upload' && req.method === 'POST') {
        const form = new formidable.IncomingForm({ maxFileSize });
        form.parse(req, async (err: any, fields: any, files: any) => {
            if (err) {
                if (err.code === 'LIMIT_FILE_SIZE') {
                    res.writeHead(400, { 'Content-Type': 'text/plain' });
                    res.end('Error: File size exceeds the maximum allowed size of 50 kB.');
                } else {
                    res.writeHead(500, { 'Content-Type': 'text/plain' });
                    res.end('Error parsing the file upload.');
                }
                return;
            }

            // Логирование информации о файле
            console.log('Files:', files);

            const file = files.file && Array.isArray(files.file) ? files.file[0] : files.file;
            if (!file) {
                res.writeHead(400, { 'Content-Type': 'text/plain' });
                res.end('No file uploaded.');
                return;
            }

            const localFilePath = file.filepath;
            const fileName = file.originalFilename;
            const fileExtension = fileName.slice(fileName.lastIndexOf('.'));
            const fileType = file.mimetype;

            // Проверка допустимого расширения и типа файла
            if (!allowedExtensions.includes(fileExtension) || !allowedMimeTypes.includes(fileType)) {
                res.writeHead(400, { 'Content-Type': 'text/plain' });
                res.end('Error: Only .docx files are allowed.');
                return;
            }

            // Логирование пути к файлу
            console.log('File path:', localFilePath);
            console.log('File name:', fileName);

            if (!localFilePath) {
                res.writeHead(500, { 'Content-Type': 'text/plain' });
                res.end('Error: file path is undefined.');
                return;
            }

            const uploadPath = 'disk:/Тестовое задание/';
            const uploadFullPath = `${uploadPath}${fileName}`;

            try {
                const uploadUrl = await getUploadUrl(accessToken, uploadFullPath);
                await uploadFile(uploadUrl, localFilePath);

                res.writeHead(200, { 'Content-Type': 'text/plain' });
                res.end(`Файл успешно загружен.\nPath: ${uploadFullPath}`);
            } catch (error) {
                console.error('Error during file upload:', error);
                res.writeHead(500, { 'Content-Type': 'text/html' });
                res.end('<p>Error performing file operations. Check the console for details.</p>');
            }
        });
    } else if (pathname === '/get-file-content' && req.method === 'GET') {
        const query = parsedUrl.query;
        const filePath = query.path as string;

        if (!filePath) {
            res.writeHead(400, { 'Content-Type': 'text/plain' });
            res.end('Error: File path is required.');
            return;
        }

        try {
            console.log('Retrieving content for file:', filePath); // Логируем путь файла
            const fileContent = await getFileContent(accessToken, filePath);
            console.log('File content retrieved successfully'); // Логируем успешное получение содержимого файла
            const mammothResult = await mammoth.extractRawText({ buffer: Buffer.from(fileContent) });
            res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
            res.end(mammothResult.value);
        } catch (error) {
            console.error('Error retrieving file content:', error); // Логируем ошибку
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Ошибка просмотра файла!\nЗагрузите файл на диск перед тем, как посмотреть его содержание.');
        }
    } else if (pathname === '/delete-file' && req.method === 'DELETE') {
        const query = parsedUrl.query;
        const fileName = query.name as string;

        if (!fileName) {
            res.writeHead(400, { 'Content-Type': 'text/plain' });
            res.end('Error: File name is required.');
            return;
        }

        const deletePath = `disk:/Тестовое задание/${fileName}`;

        try {
            await deleteFile(accessToken, deletePath);
            res.writeHead(200, { 'Content-Type': 'text/plain' });
            res.end('Файл успешно удалён!');
        } catch (error) {
            console.error('Error deleting file:', error);
            res.writeHead(500, { 'Content-Type': 'text/plain' });
            res.end('Ошибка удаления файла!\nВозможно вы ввели некорректное название. Не забудьте писать разрешение файла.\nВозможно файла с таким названием нет на диске.');
        }
    } else {
        res.writeHead(404, { 'Content-Type': 'text/html' });
        res.end('<p>Page not found</p>');
    }
};

const startServer = async () => {
    const server = http.createServer(requestListener);
    server.listen(8081, async () => {
        console.log('Server is running on http://localhost:8081');
        try {
            await execPromise('node open-browser.cjs');
        } catch (error) {
            console.error('Failed to open browser:', error);
        }
    });
};

startServer();
