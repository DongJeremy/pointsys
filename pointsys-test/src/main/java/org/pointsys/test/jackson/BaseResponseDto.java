package org.pointsys.test.jackson;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BaseResponseDto<T> implements Serializable {

    /** バージョンID */
    private static final long serialVersionUID = 1L;

    /** 会社コード */
    private String companyCode;

    /** APIコード */
    private ResultCode code = ResultCode.SUCCESS;

    /** データ */
    private T data;

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public ResultCode getCode() {
        return code;
    }

    public void setCode(ResultCode code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /** メッセージリスト */
    private List<String> messageList = new ArrayList<String>();

    /**
     * メッセージの追加
     *
     * @param msg メッセージ
     */
    public void setMessage(final String msg) {
        messageList.add(msg);
    }

    /**
     * 成功レスポンス
     *
     * @param <T> データ
     * @return レスポンスデータ
     */
    public static <T> BaseResponseDto<T> success() {
        return result(null, ResultCode.SUCCESS, ResultCode.SUCCESS.getMessage(), null);
    }

    /**
     * 成功レスポンス（レスポンスデータ）
     *
     * @param <T>  データ
     * @param data レスポンスデータ
     * @return レスポンスデータ
     */
    public static <T> BaseResponseDto<T> success(final T data) {
        return result(null, ResultCode.SUCCESS, ResultCode.SUCCESS.getMessage(), data);
    }

    /**
     * 成功レスポンス（レスポンスデータ、メッセージ）
     *
     * @param <T>     データ
     * @param data    レスポンスデータ
     * @param message メッセージ
     * @return API戻り値
     */
    public static <T> BaseResponseDto<T> success(final T data, final String message) {
        return result(null, ResultCode.SUCCESS, message, data);
    }

    /**
     * 失敗レスポンス（メッセージ）
     *
     * @param <T>     データ
     * @param message メッセージ
     * @return API戻り値
     */
    public static <T> BaseResponseDto<T> systemError(final String message) {
        return result(null, ResultCode.FAIL, message, null);
    }

    /**
     * 失敗レスポンス（リザルトコード、メッセージ）
     *
     * @param <T>     データ
     * @param code    APIコード
     * @param message メッセージ
     * @return API戻り値
     */
    public static <T> BaseResponseDto<T> error(final ResultCode code, final String message) {
        return result(null, code, message, null);
    }

    /**
     * レスポンスの作成
     *
     * @param <T>         データ
     * @param companyCode 会社コード
     * @param resultCode  APIコード
     * @param message     メッセージリスト
     * @param data        レスポンスデータ
     * @return レスポンスデータ
     */
    public static <T> BaseResponseDto<T> result(final String companyCode, final ResultCode resultCode,
            final String message, final T data) {
        BaseResponseDto<T> restResult = new BaseResponseDto<>();
        restResult.setData(data);
        restResult.setCode(resultCode);
        restResult.setMessage(message);
        return restResult;
    }
}
