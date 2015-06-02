package pkks.etf.bibliotekaef.adapters;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pkks.etf.bibliotekaef.R;
import pkks.etf.bibliotekaef.types.BookEntry;
import pkks.etf.bibliotekaef.util.BitmapUtils;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<BookEntry> mDataset;
    private Context mContext;
    private BookListInterface mListInterface;
    private boolean adderIncluded;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTitle;
        public TextView tvAuthor;
        public TextView tvISBN;
        public TextView tvDate;
        public TextView tvPageCount;
        public TextView tvStatus;

        public ImageView ivCover;

        public View parent;
        public ViewHolder(View v) {
            super(v);

            parent = v;

            tvTitle = (TextView)v.findViewById(R.id.tvTitle);
            tvAuthor = (TextView)v.findViewById(R.id.tvAuthor);
            tvISBN = (TextView)v.findViewById(R.id.tvISBN);
            tvDate = (TextView)v.findViewById(R.id.tvDate);
            tvPageCount = (TextView)v.findViewById(R.id.tvPageCount);
            tvStatus = (TextView)v.findViewById(R.id.tvStatus);

            ivCover = (ImageView)v.findViewById(R.id.ivCover);
        }
    }


    public ListAdapter(Context context, List<BookEntry> entries, BookListInterface listInterface, boolean includeAdder) {
        if ( includeAdder )
            entries.add(0, new BookEntry());
        adderIncluded = includeAdder;

        mContext = context;
        mDataset = entries;
        mListInterface = listInterface;
    }

    public ListAdapter(Context context, List<BookEntry> entries, BookListInterface listInterface) {
        entries.add(0, new BookEntry());
        adderIncluded = true;

        mContext = context;
        mDataset = entries;
        mListInterface = listInterface;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public ListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int position) {
        View v;
        if ( position == 0 && adderIncluded ) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_book_add_view, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.single_book_view, parent, false);
        }
        ViewHolder vh = new ViewHolder(v);

        v.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.abc_fade_in));
        return vh;
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if ( position == 0 && adderIncluded ) {
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListInterface.newEntryRequested();
                }
            });
        } else {
            final BookEntry cEntry = mDataset.get(position);

            holder.tvTitle.setText(String.format("%s%s", mContext.getString(R.string.title_prefix), cEntry.title));
            holder.tvAuthor.setText(String.format("%s%s", mContext.getString(R.string.author_prefix), cEntry.author));
            holder.tvISBN.setText(String.format("%s%s", mContext.getString(R.string.ibfn_prefix), cEntry.ISBN));
            holder.tvDate.setText(String.format("%s%s", mContext.getString(R.string.date_prefix), cEntry.getFormattedDate()));
            holder.tvPageCount.setText(String.format("%s%s", mContext.getString(R.string.page_count_prefix), cEntry.pageCount));
            holder.tvStatus.setText(String.format("%s%s", mContext.getString(R.string.status_prefix), cEntry.getStatus(mContext)));

            holder.ivCover.post(new Runnable() {
                @Override
                public void run() {
                    holder.ivCover.getWidth();
                    holder.ivCover.setImageBitmap(
                            BitmapUtils.decodeSampledBitmapFromPath(cEntry.coverImage.getAbsolutePath(),
                                    holder.ivCover.getWidth(),
                                    holder.ivCover.getHeight()));
                }
            });
            holder.parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListInterface.bookSelected(ListAdapter.this, cEntry);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface BookListInterface {
        void newEntryRequested();
        void bookSelected(ListAdapter adapter, BookEntry entry);
    }
}