package org.pointsys.test.jackson;

public enum ResultCode {
    /** 200:SUCCESS */
    SUCCESS(200, "請求成功"),
    /** 207:BUSINESSERR */
    BUSINESSERR(207, "ビジネスエラー"),
    /** 404:指定されたURLが見つかりませんでした */
    NOT_FOUND(404, "指定されたURLが見つかりませんでした"),
    /** 500：サーバ内部エラー */
    FAIL(500, "サーバ内部エラー");

    /** 戻り状態コード */
    private int code;

    /** 戻りメッセージ */
    private String message;

    /**
     * 初期化
     *
     * @param code    戻り状態コード
     * @param message 戻りメッセージ
     */
    ResultCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 戻りコード取得
     * 
     * @return 戻りコード
     */
    public int getCode() {
        return code;
    }

    /**
     * 戻りメッセージ取得
     * 
     * @return 戻りメッセージ
     */
    public String getMessage() {
        return message;
    }
}
