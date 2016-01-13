#!/bin/sh
username=sl_main
password=sl_main
host=192.168.18.19
port=1521
dbname=dbdev
tables="
ACTOR:Actor
ACTOR_PRIV:ActorPrivilege
ACTOR_PRIV_LOG:ActorPrivilegeLog
PI:PersonalInfo
ACCOUNT:Account
ADDR:Address
CONTACT:Contact
LOAN_APP:LoanApp
IP:IpAddress
ASSET_LIABILITY:AssetLiability
EMPLOYMENT:Employment
GUARANTOR_PERSONAL:GuarantorPerson
GUARANTOR_CORPORATE:GuarantorCorporate
DOC:Doc
DOC_FILE:DocFile
CREDIT:Credit
AUDIT:Audit
SR:Sr
SR_NOTE:SrNote
TASK:Task
TASK_NOTE:TaskNote
LOAN:Loan
SEQUENCE:Sequence
CFG_ITEM:ConfigItem
CFG:ConfigProfile
CFG_ITEM:ConfigItem
CRC_TASK:Task
ACTOR_PRIV:ActorPrivilege
WF_ROLE_ACTOR:ActorRole
ROLE:Role
ROLE_PRIV:RolePrivilege
CRC_TASK_METADATA:MetadataTaskEntity
CRC_TASK_METADATA_OPS:MetadataTaskOperation
CRC_TASK_METADARA_TAGS:MetadataTaskTag
CRC_TASK_RELATION:MetadataTaskRelation
CRC_OPERATION_LOG:OperationLog
"
_doQuery(){
  sqlplus -S $username/$password@$host:$port/$dbname <<EOF
  SET head off
  SET feedback off
  SET pages 0
  SET LONG 9999
  SELECT DBMS_METADATA.GET_DDL('TABLE','SL\$$table') FROM DUAL;
  exit;
EOF
}

query(){
  echo '<table tableName="SL$'$table'" domainObjectName="'Entity$bean'" enableCountByExample="false"
  enableUpdateByExample="false" enableDeleteByExample="false"
  enableSelectByExample="false" selectByExampleQueryId="false">'
  _doQuery | grep 'NUMBER(.*)' | sed 's/^\s\+\?(//' | while read line; do
    if echo "$line" | grep -q ' NUMBER(\*,0)' ; then
      echo "$line" | awk '{print "\t<columnOverride column="$1" javaType=\"java.lang.Long\"/>"}'
    elif echo "$line" | grep -q 'NUMBER([0-5],0)' ; then
      echo "$line" | awk '{print "\t<columnOverride column="$1" javaType=\"java.lang.Integer\"/>"}'
    elif echo "$line" | grep -q 'NUMBER([0-9]\+,0)' ; then
      echo "$line" | awk '{print "\t<columnOverride column="$1" javaType=\"java.lang.Long\"/>"}'
    fi
  done
  echo '</table>'
}

for t in $tables; do
  export table=${t%:*}
  export bean=${t#*:}
  query
done
