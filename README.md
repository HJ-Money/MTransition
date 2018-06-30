English  |  [中文文档](README_cn.md)

MTransition
===========================
MTransition is a page switching animation library on Android,it can make some complex, custom page switching animations easier to implement, and reduce development time.

## Example
![](/Introduction/1.gif)

`The design comes from:https://dribbble.com/shots/2493845-ToFind-Transition-Test`  
 
In the example above,before and after the animation is executed ** Two different Activity**.As shown below:
![](/Introduction/1.png "Activity1") ![](/Introduction/2.png "Activity2")

**As a developer,you only need to complete UI development of two activities.The intermediate transition animation is handed over to MTransiton, and MTransiton can perfectly implement the various page switching animations you need.**

For more examples, please see this repository code or[SlidingActivity](https://github.com/HJ-Money/SlidingActivity)

## Usage


MTransition is applicable to the switching animation of all pages. **The page here is not necessarily an Activity, but it can also be a Fragment, a Window, or two Views of the same Activity,but the page must be full of the entire screen. **

#### You can add the dependency to your project build.gradle file:：
```Java
compile 'com.mjun:mtransition:0.1.3'
```

Now suppose that we need to implement a transition from page A (FromPage) to page B (ToPage). First of all, we complete the UI development of page A and page B, and then we can use MTransition to complete the transition animation.

#### Basic usage

**step1**、Create an MTransition before page A opens page B and set page A's outermost view to MTransition:
```Java
final MTransition transition = MTransitionManager.getInstance().createTransition("example");
transition.fromPage().setContainer(mContainerView, new ITransitPrepareListener() {
    @Override
    public void onPrepare(MTransitionView container) {
        // Pass Views that need to be animated
        transition.fromPage().addTransitionView("icon", mIconView);
        ...
    }
});
```

**step2**、After page B is created, and before the page is displayed, set the outermost View of page B to MTransition, and tell the View which animation to be done, adjust the View order, and so on:
```Java
final MTransition transition = MTransitionManager.getInstance().getTransition("example");
transition.toPage().setContainer(mContainerView, new ITransitPrepareListener() {
  @Override
  public void onPrepare(MTransitionView container) {
      container.alpha(0f, 1f);
      MTransitionView icon = transition.toPage().addTransitionView("icon", mImageView);
      transition.fromPage().getTransitionView("icon").above(icon).transitTo(icon, true);
  }
});
```

**step3**、set the animation duration, then start MTransition.
```Java
transition.setDuration(500);
transition.start();
```

##### NOTE：
**1** If the pages A and B are Activities, because the Activity has default ActivityAnimtion, so the following code needs to be called after startActivity() and finish():
```Java
startActivity(intent);
MTranstionUtil.removeActivityAnimation(this);
```

```Java
finish();
MTranstionUtil.removeActivityAnimation(this);
```

**2**、When determining that the MTransition of the current page is no longer needed, be sure to call destroyTransition() to destroy the MTransition, otherwise a memory leak will occur:
```Java
MTransitionManager.getInstance().destoryTransition("example");
```

#### Advanced Usage
##### Reverse animation
```Java
final MTransition transition = MTransitionManager.getInstance().getTransition("example");
transition.reverse();
```

##### setProgress
You can call setProgress() to make the animation stay where you want, so you can use this to implement the animation which follows user's touch event. For details, please refer to this repository code.

##### Custom animation
MTransition has provided a bunch of basic animation apis such as translate, rotate, scale, alpha, and more. These apis already meet most of the needs, but if you need some deformation or more complex animations,
You need to customize the animation.

**step1**, implement ITransitional interface with your custom animation View

**step2**, call the replaceBy(), replace your custom animation View with the original View, and animate it during the transition animation

This can be **combined with Lottie or other animation library**, to achieve some very cool effects, as shown below, specific please refer to this warehouse code Demo5,6,7;
![](/Introduction/2.gif) ![](/Introduction/3.gif)




--------------------------------
