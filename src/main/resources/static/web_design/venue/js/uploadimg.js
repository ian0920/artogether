function openFileUpload(element) {
    // 獲取被點擊元素的 data-id 屬性，並拆分成 vneId 和 position
    const dataId = element.getAttribute('data-id');
    const [vneId, position] = dataId.split('/');

    // 設定全域變數來追蹤是哪個區域
    window.currentRegionId = { vneId, position };

    // 觸發文件上傳的輸入框
    document.getElementById('fileInput').click();
}

// 預覽選擇的圖片
function previewImage() {
    const fileInput = document.getElementById("fileInput");
    const file = fileInput.files[0];
    const preview = document.getElementById("preview");

    if (file) {
        // 檢查檔案格式是否正確
        const validImageTypes = ["image/jpeg", "image/png", "image/gif"];
        if (!validImageTypes.includes(file.type)) {
            alert("Only JPEG, PNG, and GIF files are allowed.");
            preview.style.display = "none";
            return;
        }

        // 使用 FileReader 來讀取圖片並顯示
        const reader = new FileReader();
        reader.onload = function (e) {
            preview.src = e.target.result;
            preview.style.display = "block";
        };
        reader.readAsDataURL(file);
    } else {
        preview.style.display = "none";
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

    const formData = new FormData();
    formData.append("file", file);

    fetch(UPLOAD_URL, {
        method: "POST",
        body: formData,
    })
        .then((response) => response.json())
        .then((data) => {
            document.getElementById("uploadStatus").innerText = "Upload Successful!";
        })
        .catch((error) => {
            console.error("Error uploading file:", error);
            document.getElementById("uploadStatus").innerText = "Upload Failed!";
        });
}
