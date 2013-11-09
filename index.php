<!DOCTYPE html>

<head>

<title>Tinfoil</title>
    <meta charset="UTF-8">
    <meta name="description" content="Tinfoil is a high latency onion routing client.">
    <meta name="author" content="Tinfoil Team at HackPrinceton">
<!--[if IE]>
<script src="http://html5shiv.googlecode.com/svn/trunk/html5.js"></script>
<![endif]-->
<link rel="stylesheet" href="css/layout.css" type="text/css" media="screen">
<!--fonts-->
<link href='http://fonts.googleapis.com/css?family=Maiden+Orange|Wire+One' rel='stylesheet' type='text/css'>
<!--jQuery-->
<script src="/scripts/jquery-1.8.0.min.js"></script>
<script type="text/javascript" async="" src="http://www.google-analytics.com/ga.js"></script>

<!--jQtransform-->
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.jqtransform.min.js"></script>
<!--jQueryValidationEngine-->
<link rel="stylesheet" type="text/css" href="css/validation.css" />

<!--Bootstrap, latest compiled and minified CSS -->
<link rel="stylesheet" href="//netdna.bootstrapcdn.com/bootstrap/3.0.2/css/bootstrap.min.css">
<!--Bootstrap, latest compiled and minified JavaScript -->
<script src="//netdna.bootstrapcdn.com/bootstrap/3.0.2/js/bootstrap.min.js"></script>

</head>

<!-- Le styles -->
<!--<style type="text/css">
  body {
    padding-top: 40px;
    padding-bottom: 40px;
    background-color: #dddddd;
  }

  .form-signin {
    max-width: 300px;
    padding: 10px 29px 29px;
    margin: 0 auto 20px;
    background-color: #fff;
    border: 1px solid #e5e5e5;
    -webkit-border-radius: 5px;
       -moz-border-radius: 5px;
            border-radius: 5px;
    -webkit-box-shadow: 0 1px 2px rgba(0,0,0,.05);
       -moz-box-shadow: 0 1px 2px rgba(0,0,0,.05);
            box-shadow: 0 1px 2px rgba(0,0,0,.05);
  }
  .form-signin .form-signin-heading,
  .form-signin .checkbox {
    margin-bottom: 10px;
  }
  .form-signin input[type="text"],
  .form-signin input[type="password"] {
    font-size: 16px;
    height: auto;
    margin-bottom: 15px;
    padding: 7px 9px;
  }

</style>-->
<?php
session_name("form");
session_start();

$_SESSION['n1'] = rand(1,20);
$_SESSION['n2'] = rand(1,20);
$_SESSION['expect'] = $_SESSION['n1']+$_SESSION['n2'];


$str='';
if($_SESSION['errStr'])
{
	$str='<div class="error">'.$_SESSION['errStr'].'</div>';
	unset($_SESSION['errStr']);
}

$success='';
if($_SESSION['sent'])
{
	$success='<h1>Thank you!</h1>';
	$css='<style type="text/css">#contact-form{display:none;}</style>';
	unset($_SESSION['sent']);
}
?>

<body>

<div id="wrapbox">

    <h1 class="normal" style="text-align:left;">Enter Anti-NSA Site</h1>

	<div id="wrapbox2">
    
    <form id="form" name="form" method="post" action="submit.php">
      <table width="100%" border="0" cellspacing="0" cellpadding="4px">
        <tr>
          <td><label for="email">Email</label></td>
          <td><input type="text" class="validate[required,custom[email]]" name="email" id="email" value="<?=$_SESSION['post']['email']?>" /></td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td><label for="url">URL</label></td>
          <td><input type="text" class="validate[required,custom[url]]" name="url" id="url" value="<?=$_SESSION['post']['url']?>" /></td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td><label for="latency">Latency</label></td>
          <td><select name="latency" id="latency">
            <option value="" selected="selected"> - Choose -</option>
            <option value="5">5minutes</option>
            <option value="10">10minutes</option>
            <option value="15">15minutes</option>
            <option value="30">30minutes</option>
            <option value="60">60minutes</option>
            <option value="90">90minutes</option>
          </select></td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td><label for="captcha"><?=$_SESSION['n1']?> + <?=$_SESSION['n2']?> =</label></td>
          <td><input type="text" class="validate[required,custom[onlyNumber]]" name="captcha" id="captcha" /></td>
          <td valign="top">&nbsp;</td>
        </tr>
        <tr>
          <td valign="top">&nbsp;</td>
          <td><input type="submit" name="button" id="button" value="Happiness Awaits"/>
          
          <?=$str?><img id="loading" src="img/load.gif" width="16" height="16" alt="loading" /></td>
        </tr>
      </table>
      </form>
      <?=$success?>
      
    </div>

</div>
<!--<div class="container">
  <form class="form-signin" >
    <h2 class="form-signin-heading">Enter Anti-NSA site</h2>
    <input type="text" class="input-block-level" placeholder="Email " clickev="true">
    <input type="text" class="input-block-level" placeholder="Latency" clickev="true">
    <input type="text" class="input-block-level" placeholder="URL" clickev="true">
    <button class="btn btn-large btn-primary" type="submit">Happiness awaits</button>
  </form>
</div>-->

<script type="text/javascript" src="js/jqtransform.js"></script>
<script type="text/javascript" src="js/validation.js"></script>

</body>

</html>
