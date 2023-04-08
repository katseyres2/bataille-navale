# $IsWindows = $env:OS -match 'Windows'

if ($IsLinux) {
	java Main 5000
} elseif ($IsWindows) {
	java.exe Main 5000
}