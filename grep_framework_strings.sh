#!/system/bin/sh

# Function to grep framework overlay strings
grep_framework_strings() {

  arg="$1"

  # Check if it's a URL (starts with http or https)
  if [[ "$arg" =~ ^http ]]; then
    echo "Downloading APK from URL not supported due to security restrictions."
  else
    # Handle local file path
    apk_path="$arg"

    # Check if APK exists (only for local paths)
    if [ ! -f "$apk_path" ]; then
      echo "Error: APK file not found at $apk_path"
      exit 1
    fi

    apktool d "$apk_path" resources.arsc  # Decompile resources.arsc using apktool
  fi

  # Regex to match framework overlay strings
  framework_regex="framework\/(.+)"

  # Use egrep to find lines matching the regex in resources.arsc
  egrep "$framework_regex" resources.arsc | cut -d ':' -f2 | cut -d '=' -f2- | tr -d '\n'

  # Remove temporary resources.arsc
  rm resources.arsc
}

# Check if argument is provided
if [ $# -eq 0 ]; then
  echo "Error: Please provide an APK path or URL."
  exit 1
fi

# Call the grep function with the argument
grep_framework_strings "$1"

echo "Done!"
