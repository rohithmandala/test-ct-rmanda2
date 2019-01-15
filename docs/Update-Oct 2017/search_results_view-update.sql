CREATE OR REPLACE FORCE VIEW "CTAPO"."SEARCH_RESULTS_VIEW" ("ERMS_STUDY_ID", "NCT_ID", "BRIEF_TITLE", "CONDITION", "PI_NAME", "PI_EMAIL", "STUDY_STATUS", "STUDY_STATUS_SORT_ORDER", "BRIEF_SUMMARY", "DEPT_DIV", "DEPT_DIV_ID", "PRIMARY_CATEGORY", "SECONDARY_CATEGORY", "START_DATE", "END_DATE", "FIRST_RECEIVED_DATE", "LAST_CHANGED_DATE", "OVERALL_CONTACT_NAME", "OVERALL_CONTACT_PHONE", "OVERALL_CONTACT_EMAIL", "OVERALL_CONTACT_BACKUP_NAME", "OVERALL_CONTACT_BACKUP_PHONE", "OVERALL_CONTACT_BACKUP_EMAIL", "MINIMUM_AGE", "MAXIMUM_AGE", "ELIGIBLE_GENDERS", "HEALTHY_VOLUNTEERS") AS 
  (SELECT 
  E.STUDY_ID AS ERMS_STUDY_ID,
  CS.NCT_ID,
  CS.BRIEF_TITLE,
  CS.CONDITION,
  NVL(trim(CS.INVESTIGATOR_FULL_NAME), E.PI_NAME) AS PI_NAME, 
  E.PI_EMAIL AS PI_EMAIL,
  E.STUDY_STATUS,
  SO.SORT_ID AS STUDY_STATUS_SORT_ORDER,
  CS.BRIEF_SUMMARY, 
  E.DEPT_DIV, 
  E.DEPT_DIV_SL_ID AS DEPT_DIV_ID,
  PCT.CATEGORY_TERM AS PRIMARY_CATEGORY, 
  SCT.CATEGORY_TERM AS SECONDARY_CATEGORY,
  CS.START_DATE AS START_DATE,
  CS.END_DATE AS END_DATE,
  CS.FIRST_RECEIVED_DATE AS FIRST_RECEIVED_DATE,
  CS.LAST_CHANGED_DATE AS LAST_CHANGED_DATE,
  CS.OVERALL_CONTACT_NAME,
  CS.OVERALL_CONTACT_PHONE,
  CS.OVERALL_CONTACT_EMAIL,
  CS.OVERALL_CONTACT_BACKUP_NAME,
  CS.OVERALL_CONTACT_BACKUP_PHONE,
  CS.OVERALL_CONTACT_BACKUP_EMAIL,
  CS.MINIMUM_AGE,
  CS.MAXIMUM_AGE,
  CS.GENDER AS ELIGIBLE_GENDERS,
  CS.HEALTHY_VOLUNTEERS
FROM ERMS_STUDY E
JOIN CT_STUDY CS ON CS.NCT_ID = E.NCT_ID
JOIN STUDY_STATUS_SORT_ORDER SO ON SO.STUDY_STATUS = E.STUDY_STATUS
LEFT OUTER JOIN ERMS_CATEGORY_MAPPING EM ON EM.DEPT_DIV_SL_ID = E.DEPT_DIV_SL_ID
LEFT OUTER JOIN CATEGORY PCT ON PCT.CATEGORY_ID = EM.PRIMARY_CATEGORY_ID
LEFT OUTER JOIN CATEGORY SCT ON SCT.CATEGORY_ID = EM.SECONDARY_CATEGORY_ID
WHERE E.STUDY_STATUS NOT IN (SELECT status_code FROM STUDY_STATUS_FILTER where status_system='ERMS' and status_display='N') 
AND CS.OVERALL_STATUS NOT IN (SELECT status_code FROM STUDY_STATUS_FILTER where status_system='CT' and status_display='N') 
AND (CS.EMORY_SPECIFIC_STATUS is null or CS.EMORY_SPECIFIC_STATUS NOT IN (SELECT status_code FROM STUDY_STATUS_FILTER where status_system='CT' and status_display='N')) 
--AND (CS.END_DATE is null or TO_DATE(CS.END_DATE,'MONTH YYYY') > TO_DATE(TO_CHAR(sysdate, 'MONTH YYYY'),'MONTH YYYY'))) 
AND (CS.END_DATE is null or PARSE_DATE_FROM_STRING(CS.END_DATE) > PARSE_DATE_FROM_STRING(SYSDATE)))
order by study_status_sort_order,nct_id desc;