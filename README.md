![screenshoot](https://github.com/GreyLabsDev/GradientComponents/blob/master/gradient_components_logo_round_corners.png)

[![](https://jitpack.io/v/GreyLabsDev/GradientComponents.svg)](https://jitpack.io/#GreyLabsDev/GradientComponents)

### Android library for using gradient views with non-standard animation

![gradientView](https://github.com/GreyLabsDev/GradientComponents/blob/master/gif_view.gif)
![gradientTextView](https://github.com/GreyLabsDev/GradientComponents/blob/master/gif_text.gif)

**Min. SDK: 19**
**Kotlin**

Full example you can see in sample [app](https://github.com/GreyLabsDev/GradientComponents/tree/master/app).
You can [download an APK](https://github.com/GreyLabsDev/GradientComponents/blob/master/sample.apk) of the sample app

**What's done**
1. GradientView component
2. GradientTextView component
3. Gradient vertical/horizontal orientation
4. Animation support for GradientView
5. Animation support for GradientTextView
6. Optional animation loop
7. Optional animation start after component creation
8. Animation attributes
9. Round corners support and attributes for GradientView

**TODO**
1. Angle cupport for gradient orientation
2. Gradient orientation angle animation
3. Gradient overlay for all type of views
4. Optimization for animation of GradientTextView
5. Improve animation loop realization

**GradientView, GradientTextView attributes**
 - gradientOrientation
 - baseColors
 - startColorsPositions
 - endColorsPositions
 - animationOnStart
 - animationStepDuration
 - animationLoop
 - topLeftCorner (only for GradientView)
 - topRightCorner (only for GradientView)
 - bottomLeftCorner (only for GradientView)
 - bottomRightCorner (only for GradientView)

### How to add library to your project

1. Add the JitPack repository to your build file
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2. Add the dependency 
```
dependencies {
	implementation 'com.github.GreyLabsDev:GradientComponents:0.5.0'
}
```

### How to use components in .xml layout
**GradientView**
```
<com.greylabs.gradientcomponents.component.GradientView
                android:layout_width="match_parent"
                android:layout_height="240dp"
                app:gradientOrientation="horizontal"
                app:baseColors="#91276C, #0F3368"
                app:startColorsPositions="0, 0.66"
                app:endColorsPositions="0.5, 0.99"
                app:animationStepDuration="2000"
                app:animationLoop="true"
                app:animationOnStart="true"
                app:topLeftCorner="32dp"
                app:bottomRightCorner="32dp"
        />
```

### How to use components in code
**GradientTextView**
```
       val gradientText = GradientTextView(requireContext())
       gradientText.apply {
           setOrientation(gradientOrientation)
           addGradientColor("#FFFFFF")
           addGradientColor("#91276C")
           addGradientPosition(firstColorPos, secondColorPos)
           addGradientPosition(0.9f, 1f)
           setAnimationOnStart(isAnimationEnabled)
           setLoopAnimation(isAnimationLoop)
           setAnimationStepDuration(900L)
           text = "Some text"
           initView()
      }
```
