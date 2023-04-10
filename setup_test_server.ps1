
$MINECRAFT_VERSION="1.16.5"
[System.IO.Directory]::CreateDirectory($PSScriptRoot + "/test_server/" + $MINECRAFT_VERSION)
[System.IO.Directory]::CreateDirectory($PSScriptRoot + "/test_server/build_tools")
Set-Location ./test_server/build_tools
java -jar ../../BuildTools.jar --skip-compile --compile-if-changed --rev 1.16.5 --output-dir ../1.16.5
Set-Location ../..