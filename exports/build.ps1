# $IsWindows = $env:OS -match 'Windows'
$fileName = $args[0]

Set-Location ..

if ($null -ne $fileName) {
	Write-Host "TEst"
	$oldFile = Get-ChildItem "$fileName.class" -Recurse
	if ($null -ne $oldFile) { Remove-Item $oldFile }
	Get-ChildItem "$fileName.java" -Recurse | ForEach-Object {
		if ($IsLinux) {
			javac -d exports $_.FullName
		} elseif ($IsWindows) {
			Write-Host "TEE"
			javac.exe -d exports $_.FullName
		}
	}
	Write-Host "File $fileName.java compiled."
} else {	
	Get-ChildItem *.class -Recurse | ForEach-Object {Remove-Item $_}

	if ($IsLinux) {
		javac -d exports "Main.java"
	} elseif ($IsWindows) {
		javac.exe -d exports "Main.java"
	}
}

Set-Location .\exports

Write-Host "Hello"