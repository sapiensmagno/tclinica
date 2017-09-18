package br.com.tclinica.domain.enumeration;

public enum ExamStatuses {

    LAB_REQUEST {
        @Override
        public String toString() {
            return "requested to laboratory";
        }
    },
    TO_DOC {
        @Override
        public String toString() {
            return "given to doctor";
        }
    },
    TO_PATIENT {
        @Override
        public String toString() {
            return "given to patient";
        }
    },
    ARCHIVE {
        @Override
        public String toString() {
            return "archived";
        }
    },
}
