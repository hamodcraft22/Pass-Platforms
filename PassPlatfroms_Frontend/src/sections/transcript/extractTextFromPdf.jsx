import * as pdfjs from 'pdfjs-dist/build/pdf';
import * as pdfjsWorker from 'pdfjs-dist/build/pdf.worker.mjs';

pdfjs.GlobalWorkerOptions.workerSrc = pdfjsWorker;

const extractTextFromPdf = (file) => {
    return new Promise(async (resolve, reject) => {
        if (!file || !file.type || file.type !== 'application/pdf') {
            reject(new Error('Invalid or missing PDF file'));
        }

        const reader = new FileReader();

        reader.onload = async (event) => {
            const arrayBuffer = event.target.result;

            try {
                const loadingTask = pdfjs.getDocument({data: arrayBuffer});
                const pdf = await loadingTask.promise;
                let pdfText = '';

                for (let pageNum = 1; pageNum <= pdf.numPages; pageNum++) {
                    const page = await pdf.getPage(pageNum);
                    const content = await page.getTextContent();
                    const pageText = content.items.map((item) => item.str).join(' ');
                    pdfText += pageText + '\n';
                }

                resolve(pdfText);
            } catch (error) {
                reject(new Error('Error parsing PDF: ' + error.message));
            }
        };

        reader.readAsArrayBuffer(file);
    });
};

export default extractTextFromPdf;
