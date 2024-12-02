
// 打開檔案上傳
function openFileUpload(element) {
    if (window.isUploading) {
        return;
    }


    // 獲取被點擊元素的 data-id 屬性，並拆分成 vneId 和 position
    const dataId = element.getAttribute('data-id');
    const [vneId, position] = dataId.split('/');

    // 設定全域變數來追蹤是哪個區域
    window.currentRegionId = { vneId, position };


    // 保存當前被點擊的幻燈片元素
    window.currentSlideElement = element;


    // 觸發文件上傳的輸入框
    document.getElementById('fileInput').click();
}

// 預覽選擇的圖片
function previewImage() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];


    if (file && window.currentSlideElement) {

        // 檢查檔案格式是否正確
        const validImageTypes = ["image/jpeg", "image/png", "image/gif"];
        if (!validImageTypes.includes(file.type)) {
            alert("Only JPEG, PNG, and GIF files are allowed.");

            return;
        }

        // 使用 FileReader 來讀取圖片並顯示在對應的幻燈片中
        const reader = new FileReader();
        reader.onload = function (e) {
            const imgElement = window.currentSlideElement.querySelector('img');
            imgElement.src = e.target.result;

            // 顯示 "確認上傳" 按鈕
            const uploadButton = window.currentSlideElement.querySelector('.upload-button');
            uploadButton.style.display = 'block';
            uploadButton.disabled = false; // 確保按鈕是啟用狀態
        };
        reader.readAsDataURL(file);

    }
}

// 上傳圖片的功能
function uploadFile() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];

    if (!file) {
        alert("Please select a file to upload.");
        return;
    }

    // 限制檔案類型
    const validImageTypes = ["image/jpeg", "image/png", "image/gif"];
    if (!validImageTypes.includes(file.type)) {
        alert("Only JPEG, PNG, and GIF files are allowed.");
        return;
    }

    // 構建動態上傳的 URL
    if (!window.currentRegionId) {
        alert("Upload region not specified.");
        return;
    }
    const { vneId, position } = window.currentRegionId;
    const baseUrl = window.location.origin;
    const UPLOAD_URL = `${baseUrl}/venue/images/upload/${vneId}/${position}`;


    // 禁用上傳按鈕以防止重複提交
    const uploadButton = window.currentSlideElement.querySelector('.upload-button');
    uploadButton.disabled = true;

    // 設置全域標記防止多次點擊
    window.isUploading = true;


    const formData = new FormData();
    formData.append("file", file);

    fetch(UPLOAD_URL, {
        method: "POST",
        body: formData,
    })
        .then((response) => response.json())
        .then((data) => {
            document.getElementById("uploadStatus").innerText = "Upload Successful!";

            // 隱藏 "確認上傳" 按鈕
            uploadButton.style.display = 'none';

        })
        .catch((error) => {
            console.error("Error uploading file:", error);
            document.getElementById("uploadStatus").innerText = "Upload Failed!";

            // 重新啟用上傳按鈕，允許重新嘗試
            uploadButton.disabled = false;
        })
        .finally(() => {
            // 重置全域標記
            window.isUploading = false;
        });
}

