param(
    [string]$USERNAME = "",

    [string]$DNS_PREFIX = "",

    [string]$REGION = ""
)

if ($DNS_PREFIX -eq "sp-mysql-01" -or $DNS_PREFIX -eq "vistaimobi") {
    $REGION = "sa-east-1"
} else {
    $REGION = "us-east-1"
}

$RDS_ENDPOINT = ""
$TOKEN = ""
$AWS_PROFILE = "rdsconnect"

function usage {
    Write-Host "Usage: $PSCommandPath -USERNAME <username> -DNS_PREFIX <mysql-[0-N]> -REGION <region>" -ForegroundColor Red
}

function exit_abnormal {
   usage
   exit 1
}

function get_aws_profile {
    param (
        [string]$DNS_PREFIX
    )

    if ($DNS_PREFIX -eq "office") {
        # para banco de dados office, alteramos o profile de configuração do aws cli
        return "rdsconnect-office"
    } else {
        # valor padrão para profile
        return $AWS_PROFILE
    }
}

$RDS_ENDPOINT = (Resolve-DnsName -Name "$($DNS_PREFIX).db.vistahost.com.br" -type cname).NameHost
if ([string]::IsNullOrEmpty($RDS_ENDPOINT)) {
    Write-Output "Error: '$($DNS_PREFIX)' doesn't seem to be a valid database." -ForegroundColor Red
    exit_abnormal
}

$AWS_PROFILE = get_aws_profile -DNS_PREFIX $DNS_PREFIX

$TOKEN = aws rds generate-db-auth-token --hostname $RDS_ENDPOINT --port 3306 --region $REGION --username $USERNAME --profile $AWS_PROFILE

if ($lastexitcode -eq "255") {
    aws sso login --sso-session identity-center-sso
    $TOKEN = aws rds generate-db-auth-token --hostname $RDS_ENDPOINT --port 3306 --region $REGION --username $USERNAME --profile $AWS_PROFILE
}

Write-Output $TOKEN


