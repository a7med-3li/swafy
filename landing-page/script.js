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

var APPS_SCRIPT_URL = 'https://script.google.com/macros/s/AKfycbwGOR10y49OklcupkITKt9Sgjm0pCOWn7QuWb50gR-ec6X0KSQKsurvuixNmzUQI8NZxA/exec'; // Paste your Google Apps Script web app URL here

function submitWaitlist(e) {
  e.preventDefault();
  var form = e.target;
  var role = form.getAttribute('data-type');
  var inputs = form.querySelectorAll('input');
  var wrap = form.parentElement;
  var success = wrap.parentElement.querySelector('.form-success');

  var payload = {
    fullName: inputs[0].value,
    phone: inputs[1].value,
    route: inputs[2].value,
    role: role
  };

  if (APPS_SCRIPT_URL) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', APPS_SCRIPT_URL, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
      if (wrap) wrap.style.display = 'none';
      if (success) success.classList.add('show');
    };
    xhr.onerror = function() {
      if (wrap) wrap.style.display = 'none';
      if (success) success.classList.add('show');
    };
    xhr.send(JSON.stringify(payload));
  } else {
    if (wrap) wrap.style.display = 'none';
    if (success) success.classList.add('show');
  }
  return false;
}

function submitFeedback(e) {
  e.preventDefault();
  var form = e.target;
  var textarea = form.querySelector('textarea');
  var wrap = form.parentElement;
  var success = wrap.parentElement.querySelector('.form-success');

  var payload = {
    feedback: textarea.value
  };

  if (APPS_SCRIPT_URL) {
    var xhr = new XMLHttpRequest();
    xhr.open('POST', APPS_SCRIPT_URL, true);
    xhr.setRequestHeader('Content-Type', 'application/json');
    xhr.onload = function() {
      if (wrap) wrap.style.display = 'none';
      if (success) success.classList.add('show');
    };
    xhr.onerror = function() {
      if (wrap) wrap.style.display = 'none';
      if (success) success.classList.add('show');
    };
    xhr.send(JSON.stringify(payload));
  } else {
    if (wrap) wrap.style.display = 'none';
    if (success) success.classList.add('show');
  }
  return false;
}
