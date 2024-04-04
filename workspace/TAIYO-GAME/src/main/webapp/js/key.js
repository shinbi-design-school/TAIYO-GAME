// 初期の鍵の本数
let remainingKeys = 3;

// 鍵の本数を更新して表示する関数
function updateRemainingKeys() {
  document.getElementById('remainingKeys').innerText = '鍵の本数: ' + remainingKeys;
}

// 問題を表示する関数
function displayQuestion(questionNumber) {
  const questionDiv = document.getElementById('question');
  questionDiv.innerHTML = `
    <h2>質問${questionNumber}</h2>
    <p>これはテスト問題です。</p>
    <button id="submitAnswerBtn">回答する</button>
  `;

  // 回答ボタンのクリックイベント
  document.getElementById('submitAnswerBtn').addEventListener('click', function() {
    // 問題を回答した場合、鍵の本数を減らす
    remainingKeys--;
    updateRemainingKeys();

    if (remainingKeys <= 0) {
      // 鍵が0本になったらメッセージを表示して3秒後にGAMEOVERページにリダイレクト
      questionDiv.innerHTML = '<h2>問題終了</h2><p>鍵がなくなった…</p>';
      setTimeout(function() {
        window.location.href = 'gameover.html';
      }, 3000); // 3秒後にGAMEOVERページにリダイレクト
    } else {
      // 次の問題に進む
      const nextQuestionNumber = questionNumber + 1;
      displayQuestion(nextQuestionNumber);
    }
  });
}

// 最初の問題を表示
displayQuestion(1);