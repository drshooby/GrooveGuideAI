console.log("script connected")

const userSelection = document.getElementById('full-output')
const userCompleteSelection = document.getElementById('finish-btn-container')
const inputText = document.getElementById('user-input')
const submitBtn = document.getElementById('submit-btn')
const doneBtn = document.getElementById('finish-btn')

userSelection.style.display = 'none'
userCompleteSelection.style.display = 'none'

let userText = ""
let selections = {}

const interval = setInterval(changeBorderColor, 1000, inputText)

let hue = 0

function changeBorderColor(inputBar) {
    hue += 15
    if (hue > 360) {
        hue = 0
    }
    submitBtn.style.borderColor = `hsl(${hue}, 100%, 50%)`
    inputBar.style.borderColor = `hsl(${hue}, 100%, 50%)`
}

inputText.addEventListener('input', () => {
    userText = inputText.value
})

submitBtn.addEventListener('click', submitClick)
document.addEventListener('keydown', (keyPressed) => {
    if (keyPressed.key === 'Enter') {
        submitClick()
    }
})

function submitClick() {
    userText = userText.trim()
    if (!userText) {
        alert("Empty input")
    } else {
        inputText.value = ""
        userSelection.style.display = 'flex'
        userCompleteSelection.style.display = 'flex'
        handleInput(userText)
    }
    userText = ""
}

function resetButtons() {
    const likeButtons = document.querySelectorAll('.like-btn')
    const dislikeButtons = document.querySelectorAll('.dislike-btn')
    likeButtons.forEach(element => {
        element.style.backgroundColor = 'black'
    });
    dislikeButtons.forEach(element => {
        element.style.backgroundColor = 'black'
    });
}

doneBtn.addEventListener('click', () => {
    if (Object.keys(selections).length < 3) {
        alert("Selection incomplete")
    } else {
        handleOutput(selections)
        resetButtons()
        selections = {}
        userSelection.style.display = 'none'
        userCompleteSelection.style.display = 'none'
    }
})

document.addEventListener('click', (clickEvent) => {
    if (clickEvent.target.classList.contains('like-btn') || clickEvent.target.classList.contains('dislike-btn')) {
        const btnContainer = clickEvent.target.closest('#btn-container')
        const chunkNum = btnContainer.getAttribute('button-id')
        if (clickEvent.target.classList.contains('like-btn')) {
            selections[chunkNum - 1] = true
            const dislikeButton = clickEvent.target.closest('#btn-container').querySelector('.dislike-btn');
            dislikeButton.style.backgroundColor = 'black'
            clickEvent.target.style.backgroundColor = '#65a765'
        } else if (clickEvent.target.classList.contains('dislike-btn')) {
            selections[chunkNum - 1] = false
            const likeButton = clickEvent.target.closest('#btn-container').querySelector('.like-btn');
            clickEvent.target.style.backgroundColor = '#C70039'
            likeButton.style.backgroundColor = 'black'
        }
    }
})

function handleInput(input) {
    console.log(input)
}

function handleOutput(selections) {
    console.log(selections)
}