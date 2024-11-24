<template>
    <div class="merge">
        <label for="file-upload" class="upload-label">
            <input id="file-upload" type="file" accept="application/pdf" multiple @change="handleFileUpload"
                class="upload-input" />
            Select PDF Files
        </label>

        <div class="file-list" v-if="uploadedFiles.length">
            <h3>Uploaded Files:</h3>
            <ul>
                <li v-for="(file, index) in uploadedFiles" :key="index">
                    {{ file.name }}
                </li>
            </ul>
        </div>

        <button :disabled="!uploadedFiles.length || isUploading" @click="uploadFiles" class="upload-button">
            {{ isUploading ? "Uploading..." : "Upload Files" }}
        </button>
    </div>
</template>

<script lang="ts">
import { defineComponent, ref } from "vue";
import axios from "axios";

export default defineComponent({
    name: "Merge",
    emits: ["uploadSuccess", "uploadError"],
    setup(_, { emit }) {
        const uploadedFiles = ref<File[]>([]);
        const isUploading = ref(false);

        // Handle file selection
        const handleFileUpload = (event: Event) => {
            const target = event.target as HTMLInputElement;
            const files = target.files;
            if (!files) return;

            const validFiles = Array.from(files).filter(
                (file) => file.type === "application/pdf"
            );

            if (validFiles.length !== files.length) {
                alert("Only PDF files are allowed.");
            }

            uploadedFiles.value = validFiles;
        };

        // Upload files to backend
        const uploadFiles = async () => {
            if (!uploadedFiles.value.length) return;

            const formData = new FormData();
            uploadedFiles.value.forEach((file) => {
                formData.append("pdfs", file);
            });

            isUploading.value = true;

            try {
                const response = await axios.post(
                    "http://localhost:8080/api/v1/merge",
                    formData,
                    {
                        headers: {
                            "Content-Type": "multipart/form-data",
                            "Accept": "application/octet-stream",
                        },
                        responseType: "blob",
                    }
                );
                const blob = new Blob([response.data], { type: "application/octet-stream" });

                // Create a temporary link element
                const link = document.createElement("a");
                link.href = URL.createObjectURL(blob);
                const contentDisposition = response.headers["content-disposition"];
                const fileName = contentDisposition
                    ? contentDisposition.split("filename=")[1]?.replace(/"/g, "") || "download.pdf"
                    : "download.pdf";

                link.download = fileName;
                document.body.appendChild(link);
                link.click();

                // Clean up the temporary link element
                document.body.removeChild(link);
                URL.revokeObjectURL(link.href);
                uploadedFiles.value = [];
            } catch (error) {
                emit("uploadError", error);
                alert("Failed to upload files. Please try again.");
            } finally {
                isUploading.value = false;
            }
        };

        return {
            uploadedFiles,
            isUploading,
            handleFileUpload,
            uploadFiles,
        };
    },
});
</script>

<style scoped>
.merge {
    margin: 1rem;
}

.upload-label {
    display: inline-block;
    padding: 0.5rem 1rem;
    background-color: #007bff;
    color: white;
    border-radius: 4px;
    cursor: pointer;
}

.upload-label:hover {
    background-color: #0056b3;
}

.upload-input {
    display: none;
}

.file-list {
    margin-top: 1rem;
}

.file-list ul {
    list-style: none;
    padding: 0;
}

.file-list li {
    margin: 0.5rem 0;
}

.upload-button {
    margin-top: 1rem;
    padding: 0.5rem 1rem;
    background-color: #28a745;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

.upload-button:disabled {
    background-color: #6c757d;
    cursor: not-allowed;
}
</style>