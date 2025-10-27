#!/data/data/com.termux/files/usr/bin/bash
# Generate a real Gradle wrapper for Android projects

echo "📦 Installing Gradle if not installed..."
pkg install -y gradle openjdk-17

echo "🚀 Creating Gradle wrapper for version 8.7..."
gradle wrapper --gradle-version 8.7

echo "✅ Wrapper generated successfully!"
echo "Files created:"
echo "  - gradlew"
echo "  - gradlew.bat"
echo "  - gradle/wrapper/gradle-wrapper.jar"
echo "  - gradle/wrapper/gradle-wrapper.properties"
echo
echo "Now you can run:"
echo "  git add gradlew gradlew.bat gradle/wrapper/"
echo "  git commit -m 'Add Gradle wrapper'"
echo "  git push"
