if(!(Test-Path -Path "./test_server")){
    mkdir ./test_server
}
Set-Location ./test_server
if(!(Test-Path -Path "./1.16.5")){
    mkdir ./1.16.5
}
if(!(Test-Path -Path "./build_tools")){
    mkdir ./build_tools
}
Set-Location ./build_tools
java -jar ../../BuildTools.jar --skip-compile --compile-if-changed --rev 1.16.5 --output-dir ../1.16.5
Write-Output "#By changing the setting below to TRUE you are indicating your agreement to our EULA (https://account.mojang.com/documents/minecraft_eula).`n#Mon Apr 10 18:15:24 CEST 2023`neula=true" | Out-File -FilePath ../1.16.5/eula.txt
Set-Location ../..