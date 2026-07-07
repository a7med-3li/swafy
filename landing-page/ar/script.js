function switchAudience(type) {
  document.querySelectorAll('.audience').forEach(function(el) {
    el.classList.remove('active');
  });
  document.querySelectorAll('.audience-tab').forEach(function(el) {
    el.classList.remove('active');
  });

  var panel = document.getElementById('audience-' + type);
  var tab = document.querySelector('[data-audience="' + type + '"]');
  if (panel) panel.classList.add('active');
  if (tab) tab.classList.add('active');

  var target = document.getElementById('audience-' + type);
  if (target) {
    target.scrollIntoView({ behavior: 'smooth', block: 'start' });
  }

  setTimeout(revealBenefits, 350);
}

function revealBenefits() {
  document.querySelectorAll('.audience.active .benefits li').forEach(function(li, i) {
    setTimeout(function() {
      li.classList.add('in-view');
    }, i * 120);
  });
}

var observer = new IntersectionObserver(function(entries) {
  entries.forEach(function(entry) {
    if (entry.isIntersecting) revealBenefits();
  });
}, { threshold: 0.15 });

document.addEventListener('DOMContentLoaded', function() {
  var funnel = document.querySelector('.audience.active');
  if (funnel) observer.observe(funnel);

  document.querySelectorAll('.audience').forEach(function(el) {
    observer.observe(el);
  });
});

var APPS_SCRIPT_URL = 'https://script.google.com/macros/s/AKfycbz0XpyuF2VtyeHRV46EQCA1wklqkNvigVLHd7vcsR3mlCI5rD_ZgJ_4Cu6KbTaVY9DEpQ/exec';
function postToSheet(payload, wrap, success) {
  if (APPS_SCRIPT_URL) {
    fetch(APPS_SCRIPT_URL, {
      method: 'POST',
      mode: 'no-cors',
      headers: { 'Content-Type': 'text/plain;charset=UTF-8' },
      body: JSON.stringify(payload)
    }).then(function() {
      if (wrap) wrap.style.display = 'none';
      if (success) success.classList.add('show');
    }).catch(function() {
      if (wrap) wrap.style.display = 'none';
      if (success) success.classList.add('show');
    });
  } else {
    if (wrap) wrap.style.display = 'none';
    if (success) success.classList.add('show');
  }
}

function submitWaitlist(e) {
  e.preventDefault();
  var form = e.target;
  var role = form.getAttribute('data-type');
  var inputs = form.querySelectorAll('input');
  var wrap = form.parentElement;
  var success = wrap.parentElement.querySelector('.form-success');

  postToSheet({
    fullName: inputs[0].value,
    phone: inputs[1].value,
    route: inputs[2].value,
    role: role
  }, wrap, success);
  return false;
}

function submitFeedback(e) {
  e.preventDefault();
  var form = e.target;
  var textarea = form.querySelector('textarea');
  var wrap = form.parentElement;
  var success = wrap.parentElement.querySelector('.form-success');

  postToSheet({
    feedback: textarea.value
  }, wrap, success);
  return false;
}
