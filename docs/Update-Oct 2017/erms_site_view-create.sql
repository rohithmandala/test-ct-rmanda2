CREATE OR REPLACE FORCE VIEW "CTAPO"."ERMS_SITE_VIEW" ("STUDY", "LOCATION", "TRANSFER_DATE") AS 
  (SELECT S.STUDYID STUDY, SITE.LOCATION LOCATION, SYSDATE TRANSFER_DATE
FROM STUDY S
JOIN STUDYSITE STUDYSITE ON STUDYSITE.STUDYID = S.STUDYID
JOIN ENROLLMENTLOCATION SITE ON SITE.ENROLLMENTLOCATIONID = STUDYSITE.ENROLLMENTLOCATIONID);