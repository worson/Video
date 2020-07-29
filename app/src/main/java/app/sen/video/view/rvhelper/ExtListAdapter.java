package app.sen.video.view.rvhelper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class ExtListAdapter<T, VH extends BaseViewHolder> extends ListAdapter<T, VH> {


    private static final int ITEM_TYPE_HEADER = -1;
    private static final int ITEM_TYPE_FOOTER = -2;


    private LinearLayout mHeaderLayout = null;
    private LinearLayout mFooterLayout = null;
    private int mOrientation = LinearLayout.VERTICAL;

    protected ExtListAdapter(@NonNull DiffUtil.ItemCallback<T> diffCallback) {
        super(diffCallback);
    }


    protected boolean isHeaderOrFooterPosition(int position) {
        return position == 0 || position == getItemCount() - 1;
    }


    @Override
    public int getItemCount() {
        return super.getItemCount() + 2;
    }

    public int getRealItemCount() {
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (0 == position) {
            return ITEM_TYPE_HEADER;
        } else if (position == getItemCount() - 1) {
            return ITEM_TYPE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads) {
        if (isHeaderOrFooterPosition(position)) {
            return;
        }

        onBindCustomViewHolder(holder, convertRealDataPosition(position), payloads);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
    }

    public abstract void onBindCustomViewHolder(@NonNull VH holder, int position, @NonNull List<Object> payloads);


    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VH vh = null;
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                vh = createBaseViewHolder(getHeaderLayout(parent.getContext()));
                break;
            case ITEM_TYPE_FOOTER:
                vh = createBaseViewHolder(getFooterLayout(parent.getContext()));
                break;
            default:
                vh = onCreateCustomViewHolder(parent, viewType);
        }
        return vh;
    }

    public abstract VH onCreateCustomViewHolder(@NonNull ViewGroup parent, int viewType);


    public int convertRealDataPosition(int position) {
        return position - 1;
    }


    private ViewGroup getHeaderLayout(Context context) {
        if (null == mHeaderLayout) {
            mHeaderLayout = createLinearLayout(context, mOrientation);
        }
        return mHeaderLayout;
    }

    private ViewGroup getFooterLayout(Context context) {
        if (null == mFooterLayout) {
            mFooterLayout = createLinearLayout(context, mOrientation);
        }
        return mFooterLayout;
    }

    private LinearLayout createLinearLayout(Context context, int orientation) {
        LinearLayout linearLayout = new LinearLayout(context);

        linearLayout.setOrientation(orientation);

        ViewGroup.LayoutParams layoutParams;
        if (orientation == LinearLayoutManager.VERTICAL) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        } else {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
        linearLayout.setLayoutParams(layoutParams);

        return linearLayout;
    }

    protected VH createBaseViewHolder(View view) {
        Class temp = getClass();
        Class z = null;
        while (z == null && null != temp) {
            z = getInstancedGenericKClass(temp);
            temp = temp.getSuperclass();
        }
        VH vh;
        // 泛型擦除会导致z为null
        if (z == null) {
            vh = (VH) new BaseViewHolder(view);
        } else {
            vh = createGenericKInstance(z, view);
        }
        return vh != null ? vh : (VH) new BaseViewHolder(view);
    }

    private Class getInstancedGenericKClass(Class z) {
        Type type = z.getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Type[] types = ((ParameterizedType) type).getActualTypeArguments();
            for (Type temp : types) {
                if (temp instanceof Class) {
                    Class tempClass = (Class) temp;
                    if (BaseViewHolder.class.isAssignableFrom(tempClass)) {
                        return tempClass;
                    }
                } else if (temp instanceof ParameterizedType) {
                    Type rawType = ((ParameterizedType) temp).getRawType();
                    if (rawType instanceof Class && BaseViewHolder.class.isAssignableFrom((Class<?>) rawType)) {
                        return (Class<?>) rawType;
                    }
                }
            }
        }
        return null;
    }

    private VH createGenericKInstance(Class z, View view) {
        try {
            Constructor constructor;
            // inner and unstatic class
            if (z.isMemberClass() && !Modifier.isStatic(z.getModifiers())) {
                constructor = z.getDeclaredConstructor(getClass(), View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(this, view);
            } else {
                constructor = z.getDeclaredConstructor(View.class);
                constructor.setAccessible(true);
                return (VH) constructor.newInstance(view);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addHeaderView(View header) {
        getHeaderLayout(header.getContext()).addView(header);
    }

    public void removeHeaderView(View header) {
        getHeaderLayout(header.getContext()).removeView(header);
    }

    public void addFooterView(View footer) {
        getFooterLayout(footer.getContext()).addView(footer);
    }

    public void removeFooterView(View footer) {
        getFooterLayout(footer.getContext()).removeView(footer);
    }

    @SuppressLint("WrongConstant")
    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();

        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return isHeaderOrFooterPosition(position) ? gridLayoutManager.getSpanCount() : 1;
                }
            });
        }

        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
            mOrientation = linearLayoutManager.getOrientation();
        }
    }


    @Override
    public void registerAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(new AdapterDataObserverProxy(observer, 1));
    }

}
