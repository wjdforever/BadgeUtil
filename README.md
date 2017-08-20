# BadgeUtil
Integrated util to help you using badge and managing them fast.

---
## Introduction

This util helps you to integrate badges conveniently,by `creating new custom views` instead of using a certain kind of badge view.It seems to be comlicated but is is under consideration of further extension. And in fact is not complicated at all.<br>
* You don't need to change your view structure
* It's easy to custom a new view.When you need to custom a new view,you just need to make a new copy of Demo file and change its super class to the view kind you want.Because it does not destroy any feature of your super class and it has nothing to do with its super class.
* It's easy to manage updating.With `Notifier` class you can pakage your badge data and let your `Notifer` deal with it.
* It supports all kinds of view.For example,RaddioButton,FrameLayout,LinearLayout,etc.And it support RecyclerView good.
* Easy for more extension.
  
## Usage
* 1.Create a new class and extends the type of view you want,for example,RaddioButton.
* 2.Implements BadgeUtil.Badge interface.
* 3.Override abstract methods in BadgeUtil.Badge interface.Create an BadgeDrawer object inside view class,and call badgeDrawer.draw(Canvas) method.For View you should call it in onDraw(Canvas) method.For ViewGroup you should call it in dispatchDraw(Canvas) method.
* 4.Replace your Target view type with your new customed View in your layout xml.
* 5.Call update(int number) method to update your Badge.Or if you need unified management,to do that you should instant a Notifier object and register your Badges to it,set your badge an unique id.After that you can update all badges by notifier.Or there is a lot of ways to do it.
* 6.By now your extension is finished.Infact I have already done this for you.There are two classes in view package,BadgeRaddioButton and BadgeFrameLayout.If you wanna create a new Badge Custom View of your own,What you need to do is : 
> 1) Make a new Copy of these two files.For view like ImageView make a copy of BadgeRaddioButton,and for ViewGroup like LinearLayout make a copy of BadgeFrameLayout
> 2) Change its super class to your target class.For example:
>> ```Java
>>public class BadgeImageView extends ImageView implements BadgeUtil.Badge {
>>    ...
>>    ...
>>}
>>
>>
>>public class BadgeLinearLayout extends LinearLayout implements BadgeUtil.Badge {
>>    ...
>>    ...
>>}

> and no code inside needs any modification.Easy raight?

## Something Special
All above what we've talked about goes well on normal conditon.But there is one more complex condition you should deal with if you need.<br>
`What if I want to add badges to List or Grid?`
You definetely can't call Badge.update(number) for a item view in List,Right?Because it will be reused after moment.<br>
So you need a unified management.Like this:<br>
> ```Java
> public class BadgeGridAdapter extends RecyclerView.Adapter<ViewHolder> implements BadgeUtil.UpdateAble{
>     //store your badges to a list on ViewHolder's instantiation.
>     List<Badges> badges = new ArrayList<>();
>
>     //override method `void updateBadge(List<BadgeNumber> badgeNumbers)`
>     public void updateBadge(List<BadgeNumber> badgeNumbers){
>         //update your badges by ids.
>         ...
>         ...
>     }
>
>     public void onBindViewHolder(ViewHolder holder,int position){
>         //update your badge's unique id.
>         holder.badge.setId("your unique id,you may store it in Java bean object.");
>         ...
>     }
>}
  <br>
Also there is other solutions.For Example,you could store your badge number in your Java bean object and update your badge when onBindViewHolder is called.In that case you need to update your java been objects to update your badges.

Reprint requests the document to retain the author information!Thanks.
---
<br>

## Preview
It may take a while to load gif Image.
![Preview](https://github.com/wjdforever/BadgeUtil/blob/master/preview/GIF_20170820_111700.gif)
![Preview](https://github.com/wjdforever/BadgeUtil/blob/master/preview/Screenshot_20170820-102033.png)
![Preview](https://github.com/wjdforever/BadgeUtil/blob/master/preview/Screenshot_20170820-102125.png)



