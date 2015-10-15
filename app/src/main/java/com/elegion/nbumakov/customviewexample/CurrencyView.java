package com.elegion.nbumakov.customviewexample;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Nikita Bumakov
 */
public class CurrencyView extends LinearLayout {

    private TextView mValueTextView;
    private TextView mSignTextView;
    private ImageView mDynamicImageView;

    private float mCurrencyValue;
    private CurrencySign mCurrencySign;
    private CurrencyDynamic mCurrencyDynamic;

    public CurrencyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.view_currency, this);
        setupParams(attrs, defStyle);
        setupViews();
        setupViewsData();
    }

    private void setupParams(AttributeSet attrs, int defStyle) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CurrencyView, defStyle, 0);

        mCurrencyValue = a.getFloat(R.styleable.CurrencyView_value, 0f);

        mCurrencySign = CurrencySign.values()[
                a.getInt(R.styleable.CurrencyView_currency, 0)
                ];

        mCurrencyDynamic = CurrencyDynamic.values()[
                a.getInt(R.styleable.CurrencyView_dynamic, 0)
                ];

        a.recycle();
    }

    private void setupViews() {
        mValueTextView = (TextView) findViewById(R.id.tv_currency_value);
        mSignTextView = (TextView) findViewById(R.id.tv_currency_sign);
        mDynamicImageView = (ImageView) findViewById(R.id.iv_changes_dynamic_indicator);
    }

    private void setupViewsData() {
        mValueTextView.setText(String.format("%.2f", mCurrencyValue));
        mValueTextView.setTextColor(mCurrencyValue > 0
                ? ContextCompat.getColor(getContext(), R.color.green)
                : ContextCompat.getColor(getContext(), R.color.red));

        if (mCurrencySign == CurrencySign.NONE) {
            mSignTextView.setVisibility(GONE);
        } else {
            mSignTextView.setVisibility(VISIBLE);
            mSignTextView.setText(mCurrencySign.mSign);
        }

        if (mCurrencyDynamic == CurrencyDynamic.NONE) {
            mDynamicImageView.setVisibility(GONE);
        } else {
            mDynamicImageView.setVisibility(VISIBLE);
            mDynamicImageView.setImageResource(
                    mCurrencyDynamic == CurrencyDynamic.UP
                            ? R.drawable.ic_up
                            : R.drawable.ic_down
            );
        }
    }

    public enum CurrencySign {
        NONE(""),
        USD("$"),
        EUR("€"),
        RUR("\u20BD");

        private String mSign;

        CurrencySign(String sign) {
            mSign = sign;
        }
    }

    public enum CurrencyDynamic {
        NONE,
        UP,
        DOWN
    }
}
