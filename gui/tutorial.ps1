Import-Module Microsoft.PowerShell.ConsoleGuiTools
$module = (Get-Module Microsoft.PowerShell.ConsoleGuiTools -List).ModuleBase
Add-Type -Path (Join-path $module Terminal.Gui.dll)

[Terminal.Gui.Application]::Init()

$window = [Terminal.Gui.Window]::new()
$window.Title = "Room"
[Terminal.Gui.Application]::Top.Add($window)

$label = [Terminal.Gui.Label]::new()

$inputFrame = [Terminal.Gui.FrameView]::new()
$inputFrame.Width = [Terminal.Gui.Dim]::Percent(25)
$inputFrame.Height = [Terminal.Gui.Dim]::Fill()
$inputFrame.Title = "Input"
$window.add($inputFrame)

$outputFrame = [Terminal.Gui.FrameView]::new()
$outputFrame.Width = [Terminal.Gui.Dim]::Percent(25)
$outputFrame.Height = [Terminal.Gui.Dim]::Fill()
$outputFrame.X = [Terminal.Gui.Pos]::Right($inputFrame)
$outputFrame.Title = "Output"
$window.add($outputFrame)

$gameFrame = [Terminal.Gui.FrameView]::new()
$gameFrame.Width = [Terminal.Gui.Dim]::Percent(50)
$gameFrame.Height = [Terminal.Gui.Dim]::Fill()
$gameFrame.X = [Terminal.Gui.Pos]::Right($outputFrame)
$gameFrame.Title = "Game"
$window.add($gameFrame)

[Terminal.Gui.Application]::Run()