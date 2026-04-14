const express = require('express');
const path = require('path');
const app = express();
const PORT = process.env.PORT || 8087;

// Serve all files in /public as static files
app.use(express.static(path.join(__dirname, 'public')));

// All routes serve the public folder
app.get('/', (req, res) => {
    res.sendFile(path.join(__dirname, 'public', 'index.html'));
});

app.listen(PORT, () => {
    console.log(`Frontend service running on port ${PORT}`);
});