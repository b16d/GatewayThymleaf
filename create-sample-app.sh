#!/bin/bash

# Script pour créer un exemple de ZIP d'application Thymeleaf

mkdir -p sample-app/templates
mkdir -p sample-app/static/css
mkdir -p sample-app/static/js

# Créer un template Thymeleaf
cat > sample-app/templates/index.html << 'EOF'
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sample Thymeleaf App</title>
    <link rel="stylesheet" th:href="@{/css/style.css}">
</head>
<body>
    <div class="container">
        <h1>Welcome to Sample Thymeleaf App</h1>
        <p>This is a sample application served by Thymeleaf Portal</p>
        <p th:text="${'Current time: ' + #temporals.format(#temporals.createNow(), 'yyyy-MM-dd HH:mm:ss')}"></p>

        <section>
            <h2>Features</h2>
            <ul>
                <li>Thymeleaf templating</li>
                <li>HTML5 structure</li>
                <li>CSS styling</li>
                <li>JavaScript support</li>
            </ul>
        </section>
    </div>
    <script src="js/app.js"></script>
</body>
</html>
EOF

# Créer un fichier CSS
cat > sample-app/static/css/style.css << 'EOF'
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
}

.container {
    background: white;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
    max-width: 600px;
    width: 90%;
}

h1 {
    color: #667eea;
    margin-bottom: 1rem;
}

h2 {
    color: #764ba2;
    margin-top: 1.5rem;
    margin-bottom: 1rem;
    font-size: 1.3rem;
}

p {
    color: #666;
    margin-bottom: 1rem;
    line-height: 1.6;
}

ul {
    list-style-position: inside;
    color: #666;
}

li {
    margin-bottom: 0.5rem;
}
EOF

# Créer un fichier JavaScript
cat > sample-app/static/js/app.js << 'EOF'
console.log('Sample Thymeleaf App loaded successfully!');

document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM ready');
});
EOF

# Créer le ZIP
cd sample-app
zip -r ../sample-app.zip . > /dev/null 2>&1
cd ..

echo "✅ Sample app ZIP created: sample-app.zip"
echo ""
echo "Structure:"
unzip -l sample-app.zip
echo ""
echo "You can upload this ZIP using the curl command:"
echo 'curl -X POST http://localhost:8080/api/apps/register/upload \'
echo '  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \'
echo '  -F "appName=SampleApp" \'
echo '  -F "description=Sample Thymeleaf Application" \'
echo '  -F "zipFile=@sample-app.zip"'

# Cleanup
rm -rf sample-app

