
Invoke-WebRequest -OutFile BuildTools.jar -Uri https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar

$MINECRAFT_VERSION="1.16.5"
[System.IO.Directory]::CreateDirectory($PSScriptRoot + "/test_server/$MINECRAFT_VERSION")
[System.IO.Directory]::CreateDirectory($PSScriptRoot + "/test_server/build_tools")
Set-Location ./test_server/build_tools
java -jar ../../BuildTools.jar --compile-if-changed --rev"$MINECRAFT_VERSION" --output-dir="../$MINECRAFT_VERSION"
Set-Location ../..