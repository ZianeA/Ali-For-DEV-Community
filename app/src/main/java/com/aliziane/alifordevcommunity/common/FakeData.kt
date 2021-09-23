package com.aliziane.alifordevcommunity.common

import com.aliziane.alifordevcommunity.articledetail.ArticleDetail
import com.aliziane.alifordevcommunity.home.Article
import com.aliziane.alifordevcommunity.common.network.Iso8601Utils

private val FAKE_BODY_MARKDOWN = """
---
title: React 18 Alpha is out! Now what?
published: true
date: 2021-06-08 00:00:00 UTC
tags: react
cover_image: https://images.unsplash.com/photo-1492412931596-e47811cc802f?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=1650&q=80
canonical_url: https://www.netlify.com/blog/2021/06/08/react-18-alpha-is-out-now-what/
---

Hello!

They kept us in Suspense long enough, but HECK React developers have some new features to play with!!
The best part: Almost all of the benefits of the upgrade don't require major code changes.

## The New Root API

React has always had to have some kind of root. You're probably used to seeing something like this at the top level of your applications:

```jsx
import ReactDOM from 'react-dom';
import App from 'App';

ReactDOM.render(<App />, document.getElementById('root'));
```

Pretty normal, right? Right. This `ReactDOM.render()` is now called the **Legacy Root API**. It works the exact same way as React 17. You are still allowed to keep this, but it *will* be eventually deprecated.

The **New Root API** looks a little different:

```jsx
import ReactDOM from 'react-dom';
import App from 'App';

const root = ReactDOM.createRoot(document.getElementById('root'));

root.render(<App />);
```

It's very similar! You use `ReactDOM.createRoot` instead of the old method.

With this change, a few things happen:
- The `hydrate` method is gone, and is now an option on `createRoot`
- The render callback is gone (and can now be a prop passed in to `<App />` or whatever you give to the root)

If you don't use these two functions, then you don't have to worry about their changes. If you'd like more details on them, [there's some code change examples here](https://github.com/reactwg/react-18/discussions/5) from the React core team.

By switching to the **New Root API**, you *automatically* get the new out-of-the-box improvements that come with React 18!

This change is *all you need to do* to upgrade your client to React 18. If you only use React client-side, then you're done and can skip to the installation section below! If you use server-side React or want to learn more about Suspense, keep reading.

## Suspense

Puns aside, I think we are ALL incredibly excited for Suspense finally coming out with full support. React 16 had support for it, technically, but it was never full support, and it was not very easy to understand. 

Soooo what the heck is Suspense? It's a set of functionality that allows for waiting for data to resolve before a state transition (AKA delayed transitions), reducing UI clashes while data loads (AKA placeholder throttling), and coordinating the appearance of a set of components by streaming them in order (AKA SuspenseList).

If you played with Suspense before, you might see some \"Legacy Suspense\" behavior changing, but if you'd like to try it out for the first time, the summary is that you wrap your components in a `<Suspense>` component, like so:

```jsx
<Suspense fallback={<Loading />}>
  <SomeComponentThatSuspends />
  <SomeOtherComponent />
</Suspense>
```

In this example, React will show the `<Loading />` component at first, and then will replace `<Loading />` with `<SomeComponentThatSuspends />` and `<SomeOtherComponent />` when the data is resolved in `<SomeComponentThatSuspends />`.

I want to reiterate this, because [it's different from previous versions](https://github.com/reactwg/react-18/discussions/7): Nothing inside of the `<Suspense />` component will be rendered until the data is resolved! You can see a code sample from the React core team using this [here](https://codesandbox.io/s/romantic-architecture-ht3qi?file=/src/App.js).

## Concurrent features

There are a few methods that come with React 18 that are completely opt-in. Not all of them are documented yet, but they will be as the version is optimized:

- [`startTransition`](https://github.com/reactwg/react-18/discussions/41): keep the UI responsive during a big state transition.
- `useDeferredValue`: defer updating less important parts of your app.
- `<SuspenseList>`: coordinate the order in which loading indicators show up.
- [Server-side rendering with selective hydration](https://github.com/reactwg/react-18/discussions/37): has your app load and become interactive faster.

What's nice about each of these features is that you don't have to include all of them throughout your whole application. You can opt-in to build with them in just certain parts of the app, which is very nice and flexible.

## Enough already! How do I install it?

You can use the `@alpha` tag to install React 18 right away:

```bash
npm install react@alpha react-dom@alpha
```

It will be **months**  before the Alpha reaches Beta, and more time after that until it's fully stable. You can see [more details about the roadmap here](https://github.com/reactwg/react-18/discussions/9), which also includes other functions that aren't implemented yet.

The [React Working Group](https://github.com/reactwg/react-18/discussions) has a full set of questions and discussions about these features as well, if you'd like to read more, as well as their [announcement blog post](https://reactjs.org/blog/2021/06/08/the-plan-for-react-18.html)!

'Til next time!
""".trimIndent()

val fakeUser = User(
    name = "Ali Ziane",
    avatarUrl = "https://avatars.githubusercontent.com/u/14791787?s=400&v=4"
)

val fakeArticle = Article(
    id = 101,
    title = "Jetpack Compose 1.0 is out!",
    description = "The new Android UI toolkit is finally here. See how it compares to the established view-based UI approach...",
    reactionCount = 4058,
    commentCount = 25,
    coverImageUrl = "https://cdna.artstation.com/p/assets/images/images/021/777/836/large/ali-ziane-final.jpg?1572910336",
    readTimeMinutes = 3,
    url = "https://dev.to/",
    canonicalUrl = "https://github.com/ZianeA",
    publishedAt = Iso8601Utils.parse("2021-09-08T00:00:00Z"),
    editedAt = null,
    tags = listOf("android", "kotlin", "compose"),
    author = fakeUser
)

val fakeArticleDetail = fakeArticle.run {
    ArticleDetail(
        id,
        title,
        description,
        FAKE_BODY_MARKDOWN,
        commentCount,
        reactionCount,
        coverImageUrl,
        readTimeMinutes,
        url,
        canonicalUrl,
        publishedAt,
        editedAt,
        tags,
        fakeUser,
    )
}
