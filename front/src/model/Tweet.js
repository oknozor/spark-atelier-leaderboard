class Tweet {
    constructor(tweet) {
        this.id = tweet.id
        this.text = tweet.text
        this.lang = tweet.lang
        this.user = tweet.user
        this.createdAt = Date(tweet.createdAt)
    }
}

export default Tweet
