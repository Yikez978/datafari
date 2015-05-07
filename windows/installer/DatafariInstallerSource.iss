; Script generated by the Inno Setup Script Wizard.
; SEE THE DOCUMENTATION FOR DETAILS ON CREATING INNO SETUP SCRIPT FILES!

#define MyAppName "Datafari"
#define MyAppVersion "1.1"
#define MyAppPublisher "France Labs"
#define MyAppURL "http://www.datafari.com/"

[Setup]
; NOTE: The value of AppId uniquely identifies this application.
; Do not use the same AppId value in installers for other applications.
; (To generate a new GUID, click Tools | Generate GUID inside the IDE.)
AppId={{4EE0D57D-FC41-4A5D-BE34-AEFE372F6E44}
AppName={#MyAppName}
AppVersion={#MyAppVersion}
;AppVerName={#MyAppName} {#MyAppVersion}
AppPublisher={#MyAppPublisher}
AppPublisherURL={#MyAppURL}
AppSupportURL={#MyAppURL}
AppUpdatesURL={#MyAppURL}
DefaultDirName=c:\{#MyAppName}
DefaultGroupName={#MyAppName}
DisableProgramGroupPage=yes
OutputBaseFilename=Datafari_Setup
Compression=lzma
SolidCompression=yes

[Languages]
Name: "english"; MessagesFile: "compiler:Default.isl"
    
[Files]
Source: "{#SourceDirectory}\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs
; NOTE: Don't use "Flags: ignoreversion" on any shared system files

[Code]
var
  AdminPasswordPage, MCFAdminPasswordPage: TInputQueryWizardPage;
  AdminPassword, MCFAdminPassword: String;
  isVerySilent: Boolean;
  

procedure FileReplace(SrcFile, sFrom, sTo: String);
var
		FileContent: String;
begin
    LoadStringFromFile(SrcFile, FileContent);
    StringChange (FileContent, sFrom, sTo);
    DeleteFile(SrcFile);
    SaveStringToFile(SrcFile,FileContent, True);
end;

procedure CurPageChanged(CurPageID: Integer);
  begin
  if CurPageID = 14 then 
  begin  
    FileReplace(ExpandConstant('{app}\tomcat\conf\tomcat-users.xml'),'@PASSWORD@',AdminPassword);
    FileReplace(ExpandConstant('{app}\mcf\mcf_home\properties.xml'),'@PASSWORD@',MCFAdminPassword);
  end
  end;

  
procedure InitializeWizard; 
var
  j: Integer;
begin
  { Create the pages }
  
  isVerySilent := False;
  for j := 1 to ParamCount do
    if CompareText(ParamStr(j), '/verysilent') = 0 then
    begin
      isVerySilent := True;
      Break;
    end; 

  if isVerySilent then
    Log ('VerySilent')
  else
    Log ('not VerySilent');

  MCFAdminPasswordPage := CreateInputQueryPage(wpWelcome,
    'Configuration', 'ManifoldCF Crawler',
    'Please enter password for the "admin" account of ManifoldCF');
  MCFAdminPasswordPage.Add('Password:', True);
  MCFAdminPasswordPage.Add('Please confirm password:', True);

    
  AdminPasswordPage := CreateInputQueryPage(wpWelcome,
    'Configuration', 'Datafari',
    'Please enter password for the "admin" account of Datafari');
  AdminPasswordPage.Add('Password:', True);
  AdminPasswordPage.Add('Please confirm password:', True);

end;


function NextButtonClick(CurPageID: Integer) : Boolean;
var
		AdminPassword2: String;
begin 
if CurPageID = 101 then 
begin
  Result :=  True; 
  AdminPassword := AdminPasswordPage.Values[0];
  AdminPassword2 := AdminPasswordPage.Values[1];
  if AdminPassword <>  AdminPassword2 then
  begin
    MsgBox('Warning : values are not equal', mbInformation, MB_OK);
    Result := False;  
  end 
  if AdminPassword = '' then
  begin
  	if isVerySilent then
		begin
			AdminPassword := 'password';
			Result := True;
		end
  	else
		begin
			MsgBox('Warning : password can not be blank', mbInformation, MB_OK);
			Result := False;
		end  
  end
end
else 
begin 
  Result :=  True;  
end;


if CurPageID = 100 then 
begin
  Result :=  True; 
  MCFAdminPassword := MCFAdminPasswordPage.Values[0];
  AdminPassword2 := MCFAdminPasswordPage.Values[1];
  if MCFAdminPassword <>  AdminPassword2 then
  begin
    MsgBox('Warning : values are not equal', mbInformation, MB_OK);
    Result := False;  
  end 
  if MCFAdminPassword = '' then
  begin
    if isVerySilent then
		begin
			MCFAdminPassword := 'password';
			Result := True;
		end
  	else
		begin
			MsgBox('Warning : password can not be blank', mbInformation, MB_OK);
			Result := False;
		end   
  end
end
else 
begin 
  Result :=  True;  
end;
end;



[Run]
Filename: "{app}\bin\initialize.bat"
Filename: "EXPLORER.EXE"; Parameters: "/select,{app}\bin\start-datafari.bat"
