#!/system/bin/sh

# Function to grep framework overlay strings
grep_framework_strings() {

  apktool d "$1" resources.arsc  # Decompile resources.arsc using apktool

  # Regex to match framework overlay strings
  framework_regex="framework\/(.+)"

  # Use grep to find lines matching the regex in resources.arsc
  grep -E "$framework_regex" resources.arsc | cut -d ':' -f2 | cut -d '=' -f2- | tr -d '\n'

  # Remove temporary resources.arsc
  rm resources.arsc
}

# Check if APK path is provided
if [ $# -eq 0 ]; then
  echo "Error: Please provide an APK path."
  exit 1
fi

# Call the grep function with the APK path
apk_path="$1"
grep_framework_strings "$apk_path"

echo "Done!"
