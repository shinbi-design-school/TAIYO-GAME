//------top-----//
function redirectToPage1() {
    // 音声ファイルの再生
    const audio1 = new Audio('../audio/ruledoor.mp3');
    audio1.play();
    setTimeout(() => {
        window.location.href = "rule.html";
    }, 1500); // 1.5秒
}

//-----rule-----//
function redirectToPage2() {
    // 音声ファイルの再生
    const audio1 = new Audio('../audio/ruledoor.mp3');
    audio1.play();
    setTimeout(() => {
        window.location.href = "question.html";
    }, 1500); // 1.5秒
}

//-----game-over-----//
function redirectToPage3() {
    window.location.href = "top.html";
}

//-----question-----//
const quizData = [];
const quizFileURL = '../questions.csv'; // ここに、CSVファイルのURLを記述してください


// CSVデータを読み込む関数
function loadCSV(callback) {
    const xhr = new XMLHttpRequest();
    xhr.onreadystatechange = function () {
        if (xhr.readyState === XMLHttpRequest.DONE) {
            if (xhr.status === 200) {
                const lines = xhr.responseText.split('\n').map(line => line.trim());
                lines.forEach(line => {
                    const data = line.split(',');
                    quizData.push(data);
                });
                shuffleArray(quizData); // クイズデータをシャッフルする
                callback();
            } else {
                console.error('CSVの読み込みに失敗しました');
            }
        }
    };
    xhr.open('GET', quizFileURL);
    xhr.send();
}

// 配列をシャッフルする関数
function shuffleArray(array) {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}

const quizContainer = document.getElementById('quiz-container');
const questionElement = document.getElementById('question');
const optionsElement = document.getElementById('choices');
const resultElement = document.getElementById('result');
const remainingKeysElement = document.getElementById('remainingKeys');

let currentQuestionIndex = 0;
let score = 0;
let remainingKeys = 10;

// CSVデータを読み込んで、クイズを開始する
loadCSV(startQuiz);

function startQuiz() {
    showQuestion();
}

// 残りの鍵の数を更新する関数
function updateRemainingKeys() {
    remainingKeysElement.innerHTML = "<img src='../../../../../../images/key_color.png' class='key-icon'> ✖ " + remainingKeys;
}

function shuffleOptions() {
    const currentQuestion = quizData[currentQuestionIndex];
    const options = [currentQuestion[1], currentQuestion[2], currentQuestion[3]];
    shuffleArray(options);
    // シャッフルされた選択肢をquizDataに反映
    currentQuestion[1] = options[0];
    currentQuestion[2] = options[1];
    currentQuestion[3] = options[2];
}

// 問題を表示する関数
function showQuestion() {
    const currentQuestion = quizData[currentQuestionIndex];
    questionElement.textContent = currentQuestion[0];
    optionsElement.innerHTML = '';


    // シャッフルされた選択肢をボタンとして表示
    for (let i = 1; i <= 3; i++) {
        const option = document.createElement('button');
        option.textContent = currentQuestion[i];
        option.onclick = function () {
            checkAnswer(option.textContent);
        };
        optionsElement.appendChild(option);
    }
}

// 答えをチェックする関数
function checkAnswer(answer) {
    const currentQuestion = quizData[currentQuestionIndex];
    const correctAnswer = currentQuestion[4];
    const explanation = currentQuestion[5]; // 解説を取得

    if (answer === correctAnswer) {
        score++;
        showModal('正解！', explanation); // 解説をモーダルに表示
        currentQuestionIndex++;
        if (score >= 10) {
            clear();
        }
    } else {
        showModal('不正解');
        remainingKeys--;
        updateRemainingKeys();
        if (remainingKeys <= 0) {
            gameOver();
        }
    }
}
// モーダルウィンドウの閉じるボタンにイベントリスナーを追加
const modalCloseBtn1 = document.getElementById('modal-close-btn');
modalCloseBtn1.addEventListener('click', function () {
    closeModal();
    // 正解の場合のみ次の問題に進む
    if (currentQuestionIndex < quizData.length && score < 10) {
        showQuestion();
    }
});

//-----modal-----//
// モーダルウィンドウを表示する関数
function showModal(message, explanation) {
    const modal = document.getElementById('modal');
    const modalText = document.getElementById('modal-text');
    const modalExplanation = document.getElementById('modal-explanation'); // 解説を取得

    modalText.textContent = message;
    if (explanation) {
        modalExplanation.textContent = explanation; // 解説を設定
        modalExplanation.style.display = 'block'; // 解説を表示
    } else {
        modalExplanation.style.display = 'none'; // 解説がない場合は非表示
    }
    modal.style.display = 'flex'; // モーダルウィンドウを表示
}

// モーダルウィンドウを非表示にする関数
function closeModal() {
    const modal = document.getElementById('modal');
    modal.style.display = 'none'; // モーダルウィンドウを非表示
}

// モーダルウィンドウの閉じるボタンにイベントリスナーを追加
const modalCloseBtn = document.getElementById('modal-close-btn');
modalCloseBtn.addEventListener('click', closeModal);

//clearに飛ぶ機能
function clear() {
    const modalCloseBtn = document.getElementById('modal-close-btn');
    modalCloseBtn.addEventListener('click', function () {
        window.location.href = '../HTML/clear.html';
    });
}

//GAMEOVERに飛ぶ機能
function gameOver() {
    const modalCloseBtn = document.getElementById('modal-close-btn');
    modalCloseBtn.addEventListener('click', function () {
        window.location.href = '../HTML/game_over.html';
    });
}
