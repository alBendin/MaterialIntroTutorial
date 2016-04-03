package za.co.riggaroo.materialhelptutorial.tutorial;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import za.co.riggaroo.materialhelptutorial.MaterialTutorialFragment;
import za.co.riggaroo.materialhelptutorial.R;
import za.co.riggaroo.materialhelptutorial.TutorialItem;

/**
 * @author rebeccafranks
 * @since 15/11/09.
 */
public class MaterialTutorialPresenter implements MaterialTutorialContract.UserActionsListener {
    private final Context context;
    private MaterialTutorialContract.View tutorialView;
    private List<MaterialTutorialFragment> fragments;
    private List<TutorialItem> tutorialItems;

    public MaterialTutorialPresenter(Context context, MaterialTutorialContract.View tutorialView) {
        this.tutorialView = tutorialView;
        this.context = context;
    }


    @Override
    public void loadViewPagerFragments(List<TutorialItem> tutorialItems) {
        fragments = new ArrayList<>();
        this.tutorialItems = tutorialItems;
        for (int i = 0; i < tutorialItems.size(); i++) {
            MaterialTutorialFragment helpTutorialImageFragment;
            helpTutorialImageFragment = MaterialTutorialFragment.newInstance(tutorialItems.get(i), i);
            fragments.add(helpTutorialImageFragment);
        }
        tutorialView.setViewPagerFragments(fragments);
    }

    @Override
    public void doneOrSkipClick() {
        tutorialView.showEndTutorial();
    }

    @Override
    public void nextClick() {
        tutorialView.showNextTutorial();
    }

    @Override
    public void onPageSelected(int pageNo) {
        if (pageNo >= fragments.size() - 1) {
            tutorialView.showDoneButton();
        } else {
            tutorialView.showSkipButton();
        }
    }

/*    public void transformPage(View page, float position) {
        int pagePosition = (int) page.getTag();
        if (position <= -1.0f || position >= 1.0f) { //if outside the screen do nothing

        } else if (position == 0.0f) { //centered in screen
            tutorialView.setBackgroundColor(ContextCompat.getColor(context, tutorialItems.get(pagePosition).getBackgroundColor()));
        } else { //is on screen but not in center-->user is moving image
            fadeNewColorIn(pagePosition, position);
        }
    }*/

    static final String maLog = "MainActivity.LOG_TAG";
    @Override
    //see http://developer.android.com/reference/android/support/v4/view/ViewPager.PageTransformer.html
    // Position of page relative to the current front-and-center position of the pager. 0 is front and center. 1 is one full page position to the right, and -1 is one page position to the left.
    public void transformPage(View page, float position) {
        int pagePosition = (int) page.getTag();
        int pageWidth = page.getWidth();
        ImageView background = (ImageView) page.findViewById(R.id.fragment_help_tutorial_imageview_background);
        ImageView foreground = (ImageView) page.findViewById(R.id.fragment_help_tutorial_imageview);
        if (position <= -1.0f || position >= 1.0f) { //if outside the screen do nothing
            Log.d(maLog, "case 1 outside");
            Log.d(maLog, "position: "+Float.toString(position));
//            page.setAlpha(0);
        } else if (position == 0.0f) { //centered in screen
            Log.d(maLog, "case center");

            tutorialView.setBackgroundColor(ContextCompat.getColor(context, tutorialItems.get(pagePosition).getBackgroundColor()));
        } else { //is on screen but not in center-->user is moving image
//        } else if (position < 1.0f || position >-1.0f) {
            Log.d(maLog, "computation case");
            Log.d(maLog, "position: "+Float.toString(position));
            Log.d(maLog, "translation: "+Float.toString((float) (position * 0.2 * pageWidth)));

            foreground.setTranslationX((float) (position * 0.2 * pageWidth));
            fadeNewColorIn(pagePosition, position);


        }
    }

    @Override
    public int getNumberOfTutorials() {
        if (tutorialItems != null) {
            return tutorialItems.size();
        }
        return 0;
    }

    private void fadeNewColorIn(int index, float multiplier) {
        if (multiplier < 0) {

            int colorStart = ContextCompat.getColor(context, tutorialItems.get(index).getBackgroundColor());
            if (index + 1 == fragments.size()) {
                tutorialView.setBackgroundColor(colorStart);
                return;
            }
            int colorEnd = ContextCompat.getColor(context, tutorialItems.get(index + 1).getBackgroundColor());
            int colorToSet = (int) (new ArgbEvaluator().evaluate(Math.abs(multiplier), colorStart, colorEnd));
            tutorialView.setBackgroundColor(colorToSet);
        }

    }

}
