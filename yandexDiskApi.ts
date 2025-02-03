import axios from 'axios';
import fs from 'fs';

export const getUploadUrl = async (accessToken: string, path: string): Promise<string> => {
    try {
        const response = await axios.get('https://cloud-api.yandex.net/v1/disk/resources/upload', {
            params: { path, overwrite: 'true' },
            headers: { 'Authorization': `OAuth ${accessToken}` }
        });
        return response.data.href;
    } catch (error: any) {
        console.error('Error getting upload URL:', error.message);
        if (error.response) {
            console.error('Response error:', error.response.data);
        }
        throw error;
    }
}

export const uploadFile = async (uploadUrl: string, filePath: string): Promise<void> => {
    try {
        const fileStream = fs.createReadStream(filePath);
        await axios.put(uploadUrl, fileStream, {
            headers: { 'Content-Type': 'application/octet-stream' }
        });
        console.log('File uploaded successfully.');
    } catch (error: any) {
        console.error('Error uploading file:', error.message);
        if (error.response) {
            console.error('Response error:', error.response.data);
        }
        throw error;
    }
}

export const getFileContent = async (accessToken: string, path: string): Promise<Buffer> => {
    try {
        const response = await axios.get('https://cloud-api.yandex.net/v1/disk/resources/download', {
            params: { path },
            headers: { 'Authorization': `OAuth ${accessToken}` }
        });
        const downloadUrl = response.data.href;
        const fileResponse = await axios.get(downloadUrl, { responseType: 'arraybuffer' });
        return Buffer.from(fileResponse.data);
    } catch (error: any) {
        console.error('Error getting file content:', error.message);
        if (error.response) {
            console.error('Response error:', error.response.data);
        }
        throw error;
    }
}

export const deleteFile = async (accessToken: string, path: string): Promise<void> => {
    try {
        await axios.delete('https://cloud-api.yandex.net/v1/disk/resources', {
            params: { path },
            headers: { 'Authorization': `OAuth ${accessToken}` }
        });
        console.log('File deleted successfully.');
    } catch (error: any) {
        console.error('Error deleting file:', error.message);
        if (error.response) {
            console.error('Response error:', error.response.data);
        }
        throw error;
    }
}
