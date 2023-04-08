# $IsWindows = $env:OS -match 'Windows'

if ($IsLinux) {
	java Main
} elseif ($IsWindows) {
	java.exe Main
}