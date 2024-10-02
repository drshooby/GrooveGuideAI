console.log("script connected")

const userSelection = document.getElementById('full-output')
const userCompleteSelection = document.getElementById('finish-btn-container')
const inputText = document.getElementById('user-input')
const submitBtn = document.getElementById('submit-btn')
const doneBtn = document.getElementById('finish-btn')

const inputContainer = document.getElementById('user-input-container')
const output1 = document.getElementById('output1')
const output2 = document.getElementById('output2')
const output3 = document.getElementById('output3')

const token = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const header = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

userSelection.style.display = 'none'
userCompleteSelection.style.display = 'none'

let userText = ""
let selections = {}

setInterval(changeBorderColor, 1000, inputText)

let hue = 0

function changeBorderColor(inputBar) {
    hue += 15
    if (hue > 360) {
        hue = 0
    }
    submitBtn.style.borderColor = `hsl(${hue}, 100%, 50%)`
    inputBar.style.borderColor = `hsl(${hue}, 100%, 50%)`
    doneBtn.style.borderColor = `hsl(${hue}, 100%, 50%)`
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
    const confirmed = confirm("Ready to submit?")
    if (!confirmed) {
        return;
    }
    userText = userText.trim()
    if (!userText) {
        alert("Empty input")
    } else {
        inputText.value = ""
        userSelection.style.display = 'flex'
        userCompleteSelection.style.display = 'flex'
        inputContainer.style.display = 'none'
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
        inputContainer.style.display = 'flex'
        output1.textContent = ''
        output2.textContent = ''
        output3.textContent = ''
    }
})

document.addEventListener('click', (clickEvent) => {
    if (clickEvent.target.classList.contains('like-btn') || clickEvent.target.classList.contains('dislike-btn')) {
        const btnContainer = clickEvent.target.closest('#btn-container')
        const chunkNum = btnContainer.getAttribute('button-id')
        if (clickEvent.target.classList.contains('like-btn')) {
            selections[chunkNum] = true
            const dislikeButton = clickEvent.target.closest('#btn-container').querySelector('.dislike-btn');
            dislikeButton.style.backgroundColor = 'black'
            clickEvent.target.style.backgroundColor = '#65a765'
        } else if (clickEvent.target.classList.contains('dislike-btn')) {
            selections[chunkNum] = false
            const likeButton = clickEvent.target.closest('#btn-container').querySelector('.like-btn');
            clickEvent.target.style.backgroundColor = '#C70039'
            likeButton.style.backgroundColor = 'black'
        }
    }
})

const loadingCircle = document.getElementById('loading-circle-container')

async function handleInput(userTextInput) {

    const target = '/api/recommend'
    let data = {
        input: userTextInput
    }

    loadingCircle.style.display = 'flex'

    try {
        const response = await fetch(target, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify(data)
        });

        const result = await response.json();
        output1.textContent = result.song1
        output2.textContent = result.song2
        output3.textContent = result.song3
        loadingCircle.style.display = 'none'

    } catch (error) {
        console.error('Error:', error);
    }
}

async function handleOutput(selections) {

    const target = '/api/liked'

    try {
        const response = await fetch(target, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                [header]: token
            },
            body: JSON.stringify(selections)
        });

        const result = response.ok;
        console.log(result ? "Good response" : "Bad response")

    } catch (error) {
        console.error('Error:', error);
    }
}