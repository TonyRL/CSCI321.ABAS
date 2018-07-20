package au.edu.uow.fyp01.abas.utils;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class SearchBeaconRecyclerDividerItemDecoration extends RecyclerView.ItemDecoration {

  private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
  private static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;
  private static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;
  /**
   * The divider
   */
  private Drawable divider;
  /**
   * List's orientation
   */
  private int orientation;

  /**
   * Getting default theme properties
   */
  public SearchBeaconRecyclerDividerItemDecoration(Context context, int orientation) {
    final TypedArray a = context.obtainStyledAttributes(ATTRS);
    divider = a.getDrawable(0);
    a.recycle();
    setOrientation(orientation);
  }

  @Override
  public void onDraw(Canvas c, RecyclerView parent,
      RecyclerView.State state) { // Draw divider
    if (orientation == VERTICAL_LIST) {
      drawVertical(c, parent);
    } else {
      drawHorizontal(c, parent);
    }
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
      RecyclerView.State state) {
    if (orientation == VERTICAL_LIST) {
      outRect.set(0, 0, 0, divider.getIntrinsicHeight());
    } else {
      outRect.set(0, 0, divider.getIntrinsicWidth(), 0);
    }
  }

  /**
   * Change the orientation of recycler view
   *
   * @param orientation preferred orientation
   */
  private void setOrientation(int orientation) {
    if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
      throw new IllegalArgumentException("invalid orientation");
    }
    this.orientation = orientation;
  }

  /**
   * Draw divider
   */
  private void drawVertical(Canvas c, RecyclerView parent) {
    final int left = parent.getPaddingLeft();
    final int right = parent.getWidth() - parent.getPaddingRight();
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int top =
          child.getBottom() + params.bottomMargin + Math.round(ViewCompat.getTranslationY(child));
      final int bottom = top + divider.getIntrinsicHeight();
      divider.setBounds(left, top, right, bottom);
      divider.draw(c);
    }
  }

  /**
   * Draw divider
   */
  private void drawHorizontal(Canvas c, RecyclerView parent) {
    final int top = parent.getPaddingTop();
    final int bottom = parent.getHeight() - parent.getPaddingBottom();
    final int childCount = parent.getChildCount();
    for (int i = 0; i < childCount; i++) {
      final View child = parent.getChildAt(i);
      final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
      final int left =
          child.getRight() + params.rightMargin + Math.round(ViewCompat.getTranslationX(child));
      final int right = left + divider.getIntrinsicHeight();
      divider.setBounds(left, top, right, bottom);
      divider.draw(c);
    }
  }
}

