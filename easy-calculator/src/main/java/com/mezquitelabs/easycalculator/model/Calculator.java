package com.mezquitelabs.easycalculator.model;

import java.math.BigDecimal;
import java.math.BigInteger;

import static android.text.TextUtils.isDigitsOnly;


public class Calculator {

    private OperationListener mOperationListener;
    private boolean mCurrentIsLeftOperand;
    private StringBuilder mLeftOperand;
    private StringBuilder mRightOperand;
    private String mOperator;
    private String mResult;

    public Calculator() {
        mCurrentIsLeftOperand = true;
        mLeftOperand = new StringBuilder();
        mRightOperand = new StringBuilder();
    }

    public void sumTwoNumbers(String leftOperand, String rightOperand) {
        // One of the numbers is decimal, we need to perform a decimal operation
        if (isDigitsOnly(leftOperand) && isDigitsOnly(rightOperand)) {
            sumTwoNumbers(new BigInteger(leftOperand), new BigInteger(rightOperand));
        } else {
            sumTwoNumbers(new BigDecimal(leftOperand), new BigDecimal(rightOperand));
        }
    }

    private void sumTwoNumbers(BigInteger leftOperand, BigInteger rightOperand) {
        String result = String.valueOf(leftOperand.add(rightOperand));
        finishOperation(result);
    }

    private void sumTwoNumbers(BigDecimal leftOperand, BigDecimal rightOperand) {
        String result = String.valueOf(leftOperand.add(rightOperand));
        finishOperation(result);
    }

    public void setOperationListener(OperationListener operationListener) {
        mOperationListener = operationListener;
    }

    public void appendCurrentOperand(CharSequence nextInputText) {
        if (mCurrentIsLeftOperand) {
            mLeftOperand.append(nextInputText);
        } else {
            mRightOperand.append(nextInputText);
        }
    }

    public void appendCurrentOperator(CharSequence nextInputText) {
        mOperator = nextInputText.toString();
        if (!mCurrentIsLeftOperand) {
            performOperation();
            savePreviousResultOnRightOperand(nextInputText);
        } else {
            mCurrentIsLeftOperand = false;
        }

    }



    public String getOperator() {
        return mOperator;
    }

    public void performOperation() {
        String leftOperand = getLeftOperand().toString();
        String rightOperand = getRightOperand().toString();

        switch (BasicOperations.fromString(mOperator)) {
            case ADD:
                sumTwoNumbers(leftOperand, rightOperand);
                break;
            case SUBSTRACT:
            case DIVISION:
            case PRODUCT:
                throw new UnsupportedOperationException();
        }
    }

    private void savePreviousResultOnRightOperand(CharSequence nextInputText) {
        mRightOperand.append(mResult);
        mLeftOperand.setLength(0);
        mCurrentIsLeftOperand = true;
        mOperator = nextInputText.toString();
    }

    private void finishOperation(String result) {
        mCurrentIsLeftOperand = true;
        mRightOperand.setLength(0); // Resets the value
        mOperationListener.onFinishOperation(result);
        mOperator = null;
        mResult = result;
    }

    public StringBuilder getLeftOperand() {
        return mLeftOperand;
    }

    public StringBuilder getRightOperand() {
        return mRightOperand;
    }
}
