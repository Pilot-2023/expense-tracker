#!/bin/bash

set -e # exit when an error occurs

# "i" stands for sed "improved"
sed_i() {
  local input="$1"
  # Escape the following characters: / \ . * [ ] ^ $ ( ) { } | + ?
  echo "$input" | sed 's/[\/&]/\\&/g'
}

PROPERTIES_FILE="$SERVICE-application/src/main/resources/application.yaml"

echo "Setting secrets in file $PROPERTIES_FILE"

# Declare an associative array (a Map, key -> value structure)
declare -A secrets

# key   = placeholder
# value = pipeline variable
secrets["SECRET_MONGODB_ENDPOINT"]="mongodb_endpoint_$ENV"

for placeholder in "${!secrets[@]}"; do
  pipeline_var="${secrets[$placeholder]}"
  pipeline_var_value="${!pipeline_var}"
  pipeline_var_value=$(sed_i "$pipeline_var_value")
  sed -i "s/$placeholder/$pipeline_var_value/g" "$PROPERTIES_FILE"
  echo "Replaced placeholder $placeholder by value in pipeline Environment variable $pipeline_var"
done
