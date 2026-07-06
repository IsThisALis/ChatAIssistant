[Русский](https://github.com/IsThisALis/ChatAIssistant/blob/main/README_RU.md)

# ChatAIssistant
Twitch chat AI assistant, can answer questions for you

## Installation

Official releases providing Mac/Linux executable & Windows bat scripts with libraries
You can also build latest version from source code by yourself 

## HowTo

### Use it:

You need JDK/JRE 21 installed

Download and unpack latest [release](https://github.com/IsThisALis/ChatAIssistant/releases/latest)
go to [this](https://twitchtokengenerator.com/) website, get client ID and access token 

Login to [OpenRouter](https://openrouter.ai), get API key and choose AI model
![a relative link](Images/model-name.png)

Write about yourself and how you want AI to answer 

find config.yml in bin folder and fill it:
```YAML
model: ""
api-key: "" 
client-id: ""
ask-word: ""
channel: ""
access-token: ""
bio: ""
rules: ""
api-provider: ""
api-url: "default" # default or custom
db-url: "default" # default or custom 
```

Start .bat or executable script 

### Build it:

You need to install Gradle latest version, JDK 21

```
git clone https://github.com/IsThisALis/ChatAIssistant.git 
cd ChatAIssistant
gradle build 
```

get build/distributions/ChatAIssistant .zip or .rar


# FAQ

None, open issue if have problems
