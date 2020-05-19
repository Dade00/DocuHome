package com.example.gestorehome.dbcontroller;

public class DBmyDoc {
    public  class docTable{
        public static final String FIELD_ID = "ID";
        public static final String FIELD_EXPDATE="expDate";
        public static final String FIELD_DOCTYPE="docType";
        public static final String FIELD_REMEMBERIT="remember";
        public static final String FIELD_TITOLARE="titolare";
        public static final String TBL_NAME="myDocs";
    }
    public  class picsTable{
        public static final String FIELD_ID = "ID";
        public static final String FIELD_PIC = "pic";
        public static final String FIELD_DOCID="docID";
        public static final String TBL_NAME="myPics";
    }
}
